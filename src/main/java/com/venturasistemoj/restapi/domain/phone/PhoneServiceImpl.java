package com.venturasistemoj.restapi.domain.phone;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.venturasistemoj.restapi.domain.user.User;
import com.venturasistemoj.restapi.domain.user.UserMapper;
import com.venturasistemoj.restapi.domain.user.UserRepository;
import com.venturasistemoj.restapi.exceptions.IllegalPhoneStateException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * <code>PhoneService</code> interface implementation class for managing business logic related to phone numbers.
 *
 * @author Wilson Ventura
 */

@Service
public class PhoneServiceImpl implements PhoneService {

	@Autowired private PhoneRepository phoneRepository;
	@Autowired private PhoneMapper phoneMapper;

	@Autowired private UserRepository userRepository;
	@Autowired private UserMapper userMapper;

	private static final String INCONPLETE_PHONE_DATA = "Incomplete phone number data!";

	/**
	 * <bold>Creates a new phone number for an existing user.</bold>
	 *
	 * <p>BR5: it is not possible to create a phone number without a user.
	 * If the user does not exist, throws <code>NotFoundException</code>.</p>
	 * <p>If the phone number data is incomplete, throws <code>IllegalPhoneStateException</code>.</p>
	 * <p>Returns the created phone number.</p>
	 */
	@Override
	public PhoneNumberDTO createPhoneNumber(@NotNull Long userId, @Valid PhoneNumberDTO phoneDTO)
			throws NotFoundException, IllegalPhoneStateException {

		User existingUser = getUser(userId); // BR5: throws NotFoundException

		if( ! checkPhoneState(phoneDTO))
			throw new IllegalPhoneStateException(INCONPLETE_PHONE_DATA);

		// associates phone number with user
		phoneDTO.setUserDTO(userMapper.userToUserDTO(existingUser));

		PhoneNumber savedPhoneNumber = phoneRepository.save(phoneMapper.phoneNumberDTOToPhoneNumber(phoneDTO));
		return phoneMapper.phoneNumberToPhoneNumberDTO(savedPhoneNumber);
	}

	/**
	 * <bold>Updates a phone number from an existing user's phone set.</bold>
	 *
	 * <p>If the user does not exist, throws <code>NotFoundException</code>.</p>
	 * <p>If the phone data is incomplete, it throws <code>IllegalPhoneStateException<code>.</p>
	 * <p>Returns the updated phone set.</p>
	 */
	@Override
	@Transactional(rollbackFor = IllegalArgumentException.class)
	public Set<PhoneNumberDTO> updatePhoneNumber(@NotNull Long userId, @Valid PhoneNumberDTO phoneDTO)
			throws NotFoundException, IllegalPhoneStateException {

		User existingUser = getUser(userId); // throws NotFoundException

		Set<PhoneNumber> userPhones = phoneRepository.findAllByUser(existingUser);

		if(userPhones.isEmpty())
			throw new NotFoundException(); // no phone number to update

		if( ! checkPhoneState(phoneDTO))
			throw new IllegalPhoneStateException(INCONPLETE_PHONE_DATA);

		userPhones.forEach(phone -> {
			if( phone.getPhoneId().equals(phoneDTO.getPhoneId() ) && ( ! phone.getNumber().equals(phoneDTO.getNumber())) ) {
				phone.setType(phoneDTO.getType());
				phone.setNumber(phoneDTO.getNumber());
			}
		});

		phoneRepository.saveAll(userPhones);
		return phoneMapper.phoneNumbersToPhoneNumbersDTO(userPhones);
	}

	/**
	 * <bold>Gets the phone set of an existing user.</bold>
	 *
	 * If the user does not exist or there are no registered phones, throws <code>NotFoundException</code>.
	 */
	@Override
	@Transactional(readOnly = true)
	public Set<PhoneNumberDTO> getPhonesByUserId(@NotNull Long userId) throws NotFoundException {

		User existingUser = getUser(userId); // throws NotFoundException

		if(existingUser.getPhones().isEmpty())
			throw new NotFoundException(); // no phone numbers
		else
			return phoneRepository.findAllByUser(existingUser)
					.stream()
					.map(phoneMapper::phoneNumberToPhoneNumberDTO)
					.collect(Collectors.toSet());
	}

	/**
	 * <bold>Gets all phone numbers from the database.</bold>
	 *
	 * If there are no registered phones, throws <code>NotFoundException</code>.
	 */
	@Override
	@Transactional(readOnly = true)
	public Set<PhoneNumberDTO> getPhoneNumbers() throws NotFoundException {

		Set<PhoneNumber> allPhones = phoneRepository.findAll()
				.stream()
				.collect(Collectors.toSet());

		if(allPhones.isEmpty())
			throw new NotFoundException(); // no phone numbers in database

		return phoneMapper.phoneNumbersToPhoneNumbersDTO(allPhones);
	}

	/**
	 * <bold>Removes a phone number from an existing user's phone set.</bold>
	 *
	 * <p>If the user does not exist, throws <code>NotFoundException</code>.
	 * If the phone number does not exist in the user's phone set, throws <code>NotFoundException</code>.
	 * If the data of the phone number to be removed is corrupted, throws <code>IllegalPhoneStateException</code>.</p>
	 */
	@Override
	@Transactional
	public void deletePhoneNumber(@NotNull Long userId, @Valid PhoneNumberDTO phoneDTO)
			throws NotFoundException, IllegalPhoneStateException {

		User existingUser = getUser(userId); // throws NotFoundException

		PhoneNumber existingPhone = phoneRepository.findById(phoneDTO.getPhoneId()).orElseThrow(NotFoundException::new);

		if( ! checkPhoneState(phoneDTO))
			throw new IllegalPhoneStateException(INCONPLETE_PHONE_DATA);

		existingUser.getPhones().remove(existingPhone); // disassociates phone number from user phone set
		phoneRepository.deleteById(existingPhone.getPhoneId()); // delete this user's phone number

	}

	// Checks the existence of the user to associate the phone number.
	@Transactional(readOnly = true)
	private User getUser(Long userId) throws NotFoundException {

		Optional<User> optionalUser = userRepository.findById(userId);

		if(optionalUser.isPresent())
			return optionalUser.get();
		else
			throw new NotFoundException();
	}

	// checks the consistency of phone number data.
	private boolean checkPhoneState(PhoneNumberDTO phoneDTO) {

		if(phoneDTO.getType() == null || phoneDTO.getNumber() == null)
			return false;

		return true;
	}

}
