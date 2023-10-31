package com.venturasistemoj.restapi.domain.address;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.venturasistemoj.restapi.domain.user.User;
import com.venturasistemoj.restapi.domain.user.UserMapper;
import com.venturasistemoj.restapi.domain.user.UserRepository;
import com.venturasistemoj.restapi.exceptions.IllegalAddressStateException;
import com.venturasistemoj.restapi.exceptions.IllegalOperationException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * <code>AddressService</code> interface implementation class to manage business logic related to adresses.
 *
 * @author Wilson Ventura
 */

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired private AddressRepository addressRepository;
	@Autowired private AddressMapper addressMapper;

	@Autowired private UserRepository userRepository;
	@Autowired private UserMapper userMapper;

	private static final String INCONPLETE_ADDRESS_DATA = "Incomplete address data!";
	private static final String EXISTING_ADDRESS = "User already has a registered address!";

	/**
	 * <bold>Creates a new address for an existing user.</bold>
	 *
	 * <p>BR3: it is not possible to create an address without an existing user.
	 * If the user does not exist, throws <code>NotFoundException</code>.</p>
	 * <p>BR4: it is not possible to create more than one address for a user.
	 * Otherwise, throws <code>IllegalOperationException</code>.</p>
	 * <p>If the address data is incomplete, throws <code>IllegalAddressStateException</code>.</p>
	 * <p>Returns the created address.</p>
	 */
	@Override
	public AddressDTO createAddress(@NotNull Long userId, @Valid AddressDTO addressDTO)
			throws NotFoundException, IllegalOperationException, IllegalAddressStateException {

		User existingUser = getUser(userId); // BR3: throws NotFoundException

		if(existingUser.getAddress() != null)
			throw new IllegalOperationException(EXISTING_ADDRESS); // BR4

		if( ! checkAddressState(addressDTO))
			throw new IllegalAddressStateException(INCONPLETE_ADDRESS_DATA);

		// associates address with user
		addressDTO.setUserDTO(userMapper.userToUserDTO(existingUser));

		Address savedAddress = addressRepository.save(addressMapper.addressDTOToAddress(addressDTO));
		return addressMapper.addressToAddressDTO(savedAddress);
	}

	/**
	 * <bold>Updates and overwrites an existing user's address.</bold>
	 *
	 * <p>If the user or address does not exist, throws <code>NotFoundException</code>.</p>
	 * <p>If the address data is incomplete, throws <code>IllegalAddressStateException</code>.</p>
	 * <p>Returns the updated address.</p>
	 */
	@Override
	@Transactional(rollbackFor = IllegalAddressStateException.class)
	public AddressDTO updateAddress(@NotNull Long userId, @Valid AddressDTO addressDTO)
			throws NotFoundException, IllegalAddressStateException {

		User existingUser = getUser(userId); // throws NotFoundException "Nonexistent user or address!"

		Address existingAddress = addressRepository.findByUser(existingUser);
		if(existingAddress == null)
			throw new NotFoundException();

		if( ! checkAddressState(addressDTO))
			throw new IllegalAddressStateException(INCONPLETE_ADDRESS_DATA);

		existingAddress.setPublicPlace(addressDTO.getPublicPlace());
		existingAddress.setStreetAddress(addressDTO.getStreetAddress());
		existingAddress.setComplement(addressDTO.getComplement());
		existingAddress.setCity(addressDTO.getCity());
		existingAddress.setState(addressDTO.getState());
		existingAddress.setZipCode(addressDTO.getZipCode());

		Address savedAddress = addressRepository.save(existingAddress);
		return addressMapper.addressToAddressDTO(savedAddress);
	}

	/**
	 * <bold>Gets the address of an existing user.</bold>
	 *
	 * <p>If the user or address does not exist, throws <code>NotFoundException</code>.</p>
	 */
	@Override
	@Transactional(readOnly = true)
	public AddressDTO getAddressByUserId(@NotNull Long userId) throws NotFoundException {

		User existingUser = getUser(userId); // throws NotFoundException "Nonexistent user or address!"

		if(existingUser.getAddress() != null)
			return addressMapper.addressToAddressDTO(addressRepository.findByUser(existingUser));
		else
			throw new NotFoundException();
	}

	/**
	 * <bold>Gets all addresses from the database.</bold>
	 *
	 * <p>If there is no registered address, throws <code>NotFoundException</code>.</p>
	 */
	@Override
	@Transactional(readOnly = true)
	public List<AddressDTO> getAdresses() throws NotFoundException {

		List<Address> allAdresses = addressRepository.findAll();

		if(allAdresses.isEmpty())
			throw new NotFoundException();

		return addressMapper.adressesToAdressesDTO(allAdresses);
	}

	/**
	 * <bold>Removes an existing user's address.</bold>
	 *
	 * <p>If the user ou address does not exist, throws <code>NotFoundException</code>.</p>
	 * <p>Returns HTTP status 204 No Content.</p>
	 */
	@Override
	@Transactional
	public void deleteAddress(@NotNull Long userId) throws NotFoundException {

		User existingUser = getUser(userId); // throws NotFoundException "Nonexistent user or address!"

		Address existingAddress = addressRepository.findByUser(existingUser);

		if(existingAddress != null ) {
			existingUser.setAddress(null); // disassociates the user's address
			addressRepository.delete(existingAddress); // delete the user's address
		} else
			throw new NotFoundException();
	}

	// Checks the existence of the user to associate the address.
	@Transactional(readOnly = true)
	private User getUser(Long userId) throws NotFoundException {

		Optional<User> optionalUser = userRepository.findById(userId);

		if(optionalUser.isPresent())
			return optionalUser.get();
		else
			throw new NotFoundException();
	}

	// Checks address data consistency.
	private boolean checkAddressState(AddressDTO addressDTO) {

		if( addressDTO.getPublicPlace() == null
				|| addressDTO.getStreetAddress() == null
				|| addressDTO.getCity() == null
				|| addressDTO.getState() == null
				|| addressDTO.getZipCode() == null )
			return false;

		return true;
	}

}
