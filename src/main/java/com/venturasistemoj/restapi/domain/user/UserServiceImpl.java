package com.venturasistemoj.restapi.domain.user;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.venturasistemoj.restapi.exceptions.IllegalUserStateException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * <code>UserService</code> interface implementation class to manage business logic related to users.
 *
 * @author Wilson Ventura
 */

@Service
public class UserServiceImpl implements UserService {

	private final String CPF_INCOMPATIBILITY = "CPF incompatibility: same user with different CPF ou different user with same cpf!";
	private final String INCOMPLETE_USER_DATA = "Incomplete user data!";

	@Autowired private UserRepository userRepository;
	@Autowired private UserMapper userMapper;

	/**
	 * <bold>Creates a new user./<bold>
	 *
	 * <p>Converts <code>UserDTO</code> to User with <code>UserMapper/<code> and save this <code>User</code> in the database.
	 * Converts the saved <code>User</code> to <code>UserDTO</code> and returns this DTO.</p>
	 * <p>BR1: If the user data is corrupted, throws <code>IllegalUserStateException</code>.</p>
	 * <p>BR2: If the same user is being registered with a different CPF or a different user is being registered with the
	 * same CPF, throws <code>IllegalArgumentException</code>.</p>
	 * <p>Returns the created user.</p>
	 */
	@Override
	public UserDTO createUser(@Valid UserDTO userDTO) throws IllegalArgumentException, IllegalUserStateException {

		if( ! checkUserState(userDTO))
			throw new IllegalUserStateException(INCOMPLETE_USER_DATA); // BR1

		if( userRepository.findByCpf(userDTO.getCpf()) != null || ! checkCpf(userDTO))
			throw new IllegalArgumentException(CPF_INCOMPATIBILITY); // BR2

		User user = userMapper.userDTOToUser(userDTO);
		User savedUser = userRepository.save(user);

		return userMapper.userToUserDTO(savedUser);
	}

	/**
	 * <bold>Updates user data.</bold>
	 *
	 * <p>Checks if the user exists in the database with the given <code>userId</code>, updates the user information with
	 * the received <code>UserDTO</code> data, saves the changes in the database, converts the updated user to DTO with
	 * <code>UserMapper</code> and returns this DTO.</p>
	 * <p>If the user does not exist, throws <code>NotFoundException</code>.</p>
	 * <p>BR1: If the user data is corrupted, throws <code>IllegalUserStateException</code>.</p>
	 * <p>BR2: If the same user is being registered with a different CPF or a different user is being registered with the
	 * same CPF, throws <code>IllegalArgumentException</code>./<p>
	 * <p>Returns the updated user.</p>
	 */
	@Override
	@Transactional(rollbackFor = {IllegalArgumentException.class, IllegalUserStateException.class })
	public UserDTO updateUser(@NotNull Long userId, @Valid UserDTO userDTO)
			throws NotFoundException, IllegalArgumentException, IllegalUserStateException {

		User existingUser = userRepository.findById(userId).orElseThrow(NotFoundException::new);

		if( ! checkUserState(userDTO))
			throw new IllegalUserStateException(INCOMPLETE_USER_DATA); // BR1

		if( ! checkCpf(userDTO))
			throw new IllegalUserStateException(CPF_INCOMPATIBILITY); // BR2

		existingUser.setName(userDTO.getName());
		existingUser.setSurName(userDTO.getSurName());
		existingUser.setBirthDate(userDTO.getBirthDate());
		existingUser.setCpf(userDTO.getCpf());
		existingUser.setEmail(userDTO.getEmail());

		User updatedUser = userRepository.save(existingUser);
		return userMapper.userToUserDTO(updatedUser);
	}

	/**
	 * <bold>Gets an especific user.<bold>
	 *
	 * <p>Searches for the user in the database with the given <code>id</code>, converts <code>User</code> to
	 * <code>UserDTO</code> with <code>UserMapper</code> and returns this DTO.</p>
	 */
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUserById(@NotNull Long id) throws NotFoundException {

		/**
		 * <p><code>Optional.map</code> converts <code>Optional<User></code> to <code>Optional<UserDTO></code> using the
		 * <code>userMapper</code> method reference.</p>
		 * <p><code>Optional.orElseThrow</code> throws NotFoundException if the user is not found.</p>
		 */
		return userRepository.findById(id)
				.map(userMapper::userToUserDTO)
				.orElseThrow(NotFoundException::new);
	}

	/**
	 * <bold>Gets all database users.</bold>
	 *
	 * <p>Converts the list of <code>User</code> into a list of <code>UserDTO</code>
	 * with <code>UserMapper</code> and returns that list./<p>
	 * <p>If there are no users in the database, throws <code>NotFoundException</code>.</p>
	 */
	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> getUsers() throws NotFoundException {

		if(userRepository.findAll().isEmpty())
			throw new NotFoundException();

		return userMapper.usersToUsersDTO(userRepository.findAll());
	}

	/**
	 * <bold>Removes an especific user.<bold>
	 *
	 * <p>Checks if the user exists with the given <code>userId</code> and removes the user from the database. If the
	 * user has an associated address and/or telephone number, these will be removed in cascade.</p>
	 * <p>If the user does not exist, it throws <code>NotFoundException</code>.</p>
	 * <p>Returns HTTP status 204 No Content.</p>
	 */
	@Override
	@Transactional
	public void deleteUser(@NotNull Long userId) throws NotFoundException {

		User existingUser = userRepository.findById(userId).orElseThrow(NotFoundException::new);
		userRepository.delete(existingUser);
	}

	// checks user data consistency
	private boolean checkUserState(UserDTO userDTO) {

		if( userDTO.getName() == null
				|| userDTO.getSurName() == null
				|| userDTO.getBirthDate() == null
				|| userDTO.getCpf() == null
				|| userDTO.getEmail() == null )
			return false;

		return true;
	}

	// checks same user with different CPF or different user with same CPF
	private boolean checkCpf(UserDTO userDTO) {

		AtomicBoolean check = new AtomicBoolean(true);

		userRepository.findAll().forEach(user -> {

			if (user.getName().equals(userDTO.getName())
					&& user.getSurName().equals(userDTO.getSurName())
					&& user.getBirthDate().equals(userDTO.getBirthDate())
					&& user.getEmail().equals(userDTO.getEmail())
					&& !user.getCpf().equals(userDTO.getCpf()))
				check.set(false);
			else if (!user.getName().equals(userDTO.getName())
					&& !user.getSurName().equals(userDTO.getSurName())
					&& !user.getBirthDate().equals(userDTO.getBirthDate())
					&& !user.getEmail().equals(userDTO.getEmail())
					&& user.getCpf().equals(userDTO.getCpf()))
				check.set(false);
			else
				check.set(true);
		}); // forEach

		return check.get();
	}
}
