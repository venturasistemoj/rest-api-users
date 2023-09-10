package com.venturasistemoj.restapi.domain.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * [EN] Class implementing the UserService interface for managing business logic related to users.
 *
 * [PT] Classe de implementação da interface UserService para gerenciar a lógica de negócios relacionada aos usuários.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@Service
public class UserServiceImpl implements UserService {

	@Autowired private UserRepository userRepository;
	@Autowired private UserMapper userMapper;

	/**
	 * Cria um usuário.
	 * Converte UserDTO em User com UserMapper, salva User na base de dados.
	 * Converte o User salvo em UserDTO e retorna esse DTO.
	 * RN1: Caso os dados do usuário estejam corrompidos ou possua cpf duplicado, lança IllegalUserStateException.
	 * RN2: Caso o mesmo usuário esteja sendo cadastrado com cpf diferente, lança IllegalArgumentException.
	 */
	@Override
	@Transactional
	public UserDTO createUser(@Valid UserDTO userDTO) throws IllegalArgumentException {

		List<User> databaseUsers = userRepository.findAll();
		databaseUsers.forEach(user -> {
			if( ! checkUserCpf(user, userDTO))
				throw new IllegalArgumentException(); // RN2
		});

		if(userRepository.findByCpf(userDTO.cpf()) != null || ! checkUserState(userDTO))
			throw new IllegalUserStateException("Dados do usuário incompletos ou cpf duplicado!"); // RN1

		User user = userMapper.userDTOToUser(userDTO);
		User savedUser = userRepository.save(user);

		return userMapper.userToUserDTO(savedUser);
	}

	/**
	 * Atualiza os dados de um usuário
	 * Verifica se o usuário existe na base de dados com o ID fornecido, atualiza as informações do usuário
	 * com os dados do UserDTO recebido, salva as alterações na base de dados, converte o User atualizado em UserDTO
	 * com UserMapper e retorna esse DTO.
	 * Caso o usuário não exista, lança NotFoundException.
	 * Caso userDTO esteja corrompido, lança IllegalUserStateException.
	 * RN: Caso o mesmo usuário esteja sendo atualizado com outro cpf, lança IllegalArgumentException.
	 */
	@Override
	@Transactional(rollbackFor = IllegalArgumentException.class)
	public UserDTO updateUser(@NotNull Long userId, @Valid UserDTO userDTO)
			throws NotFoundException, IllegalArgumentException, IllegalUserStateException {

		User existingUser = userRepository.findById(userId).orElseThrow(NotFoundException::new);

		if( ! checkUserState(userDTO))
			throw new IllegalUserStateException("Dados do usuário incompletos!");

		if( ! checkUserCpf(existingUser, userDTO))
			throw new IllegalArgumentException();

		existingUser.setName(userDTO.name());
		existingUser.setSurName(userDTO.surName());
		existingUser.setBirthDate(userDTO.birthDate());
		existingUser.setCpf(userDTO.cpf());
		existingUser.setEmail(userDTO.email());

		User updatedUser = userRepository.save(existingUser);
		return userMapper.userToUserDTO(updatedUser);
	}

	/**
	 * Busca o usuário na base de dados pelo ID fornecido, converte User em UserDTO com UserMapper e retorna esse DTO.
	 */
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUserById(@NotNull Long id) throws NotFoundException {

		/* Optional map converte Optional<User> em Optional<UserDTO> usando a referência de método de userMapper.
		 * Optional orElseThrow lança NotFoundException caso o usuário não seja encontrado.
		 */
		return userRepository.findById(id)
				.map(userMapper::userToUserDTO)
				.orElseThrow(NotFoundException::new);
	}

	/**
	 * Obtém todos os usuários da base de dados, converte a lista de User em uma lista de UserDTO com UserMapper e
	 * retorna essa lista.
	 * @throws NotFoundException
	 */
	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> getUsers() throws NotFoundException {

		if(userRepository.findAll().isEmpty())
			throw new NotFoundException();

		return userMapper.usersToUsersDTO(userRepository.findAll());
	}

	/**
	 * Verifica se o usuário existe com o ID fornecido e remove o usuário da base de dados.
	 * Caso o usuário não exista, lança NotFoundException.
	 */
	@Override
	@Transactional
	public void deleteUser(@NotNull Long userId) throws NotFoundException {

		User existingUser = userRepository.findById(userId).orElse(null);
		if(existingUser != null)
			userRepository.delete(existingUser);
	}

	// verifica a consistência dos dados do usuário
	private boolean checkUserState(UserDTO userDTO) {
		if( userDTO.name() == null
				|| userDTO.surName() == null
				|| userDTO.birthDate() == null
				|| userDTO.cpf() == null
				|| userDTO.email() == null )
			return false;

		return true;
	}

	// verifica duplicidade de usuário com cpf diferente
	private boolean checkUserCpf(User existingUser, UserDTO userDTO) {

		if( userDTO.name().equals(existingUser.getName())
				&& userDTO.surName().equals(existingUser.getSurName())
				&& userDTO.birthDate().equals(existingUser.getBirthDate())
				&& userDTO.email().equals(existingUser.getEmail())
				&& ! userDTO.cpf().equals(existingUser.getCpf()) )
			return false;

		return true;
	}

}
