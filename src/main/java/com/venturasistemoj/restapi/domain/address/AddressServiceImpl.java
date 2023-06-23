package com.venturasistemoj.restapi.domain.address;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.venturasistemoj.restapi.domain.user.User;
import com.venturasistemoj.restapi.domain.user.UserMapper;
import com.venturasistemoj.restapi.domain.user.UserRepository;

/**
 * [EN] Class implementing the AddressService interface for managing business logic related to adresses.
 *
 * [PT] Classe de implementação da interface AddressService para gerenciar a lógica de negócios relacionada a endereços.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired private AddressRepository addressRepository;
	@Autowired private AddressMapper addressMapper;

	@Autowired private UserRepository userRepository;
	@Autowired private UserMapper userMapper;

	/**
	 * Cria um novo endereço para um usuário existente.
	 * Caso o usuário não exista, lança NotFoundException.
	 * Caso os dados do endereço estejam corrompidos ou exista endereço cadastrado, lança IllegalArgumentException.
	 * RN3: não é possível criar endereço sem usuário.
	 * RN4: não é possível criar mais de um endereço para um usuário.
	 */
	@Override
	public AddressDTO createAddress(@NotNull Long userId, @Valid AddressDTO addressDTO)
			throws IllegalStateException, NotFoundException, IllegalAddressStateException {

		User existingUser = checkUser(userId); // RN3

		if(existingUser.getAddress() != null)
			throw new IllegalStateException(); // RN4

		if( ! checkAddressState(addressDTO))
			throw new IllegalAddressStateException("Dados do endereço incompletos!");

		addressDTO.setUserDTO(userMapper.userToUserDTO(existingUser)); // associa usuário a endereço
		Address savedAddress = addressRepository.save(addressMapper.addressDTOToAddress(addressDTO));
		return addressMapper.addressToAddressDTO(savedAddress);
	}

	/**
	 * Atualiza e sobrescreve o endereço de um usuário existente.
	 * Caso o usuário não exista, lança NotFoundException.
	 * Caso os dados do endereço estejam corrompidos, lança IllegalArgumentException.
	 * Retorna o endereço atualizado.
	 */
	@Override
	@Transactional(rollbackFor = IllegalArgumentException.class)
	public AddressDTO updateAddress(@NotNull Long userId, @Valid AddressDTO addressDTO)
			throws NotFoundException, IllegalArgumentException, IllegalAddressStateException {

		User existingUser = checkUser(userId);
		Address existingAddress = addressRepository.findByUser(existingUser);

		if(existingAddress == null)
			throw new NotFoundException();

		if( ! checkAddressState(addressDTO))
			throw new IllegalAddressStateException("Dados do endereço incompletos!");

		if(existingAddress.equals(addressMapper.addressDTOToAddress(addressDTO)))
			throw new IllegalArgumentException();

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
	 * Obtém o endereço de um usuário existente.
	 * Caso o usuário não exista, lança NotFoundException.
	 */
	@Override
	@Transactional(readOnly = true)
	public AddressDTO getAddressByUserId(@NotNull Long userId) throws NotFoundException {

		User existingUser = checkUser(userId);

		if(existingUser.getAddress() != null)
			return addressMapper.addressToAddressDTO(addressRepository.findByUser(existingUser));
		else
			throw new NotFoundException();
	}

	/**
	 * Obtém todos os endereços da base de dados.
	 * @throws NotFoundException
	 */
	@Override
	@Transactional(readOnly = true)
	public List<AddressDTO> getAdresses() throws NotFoundException {

		if(addressRepository.findAll().isEmpty())
			throw new NotFoundException();

		return addressMapper.adressesToAdressesDTO(addressRepository.findAll());
	}

	/**
	 * Remove o endereço de um usuário existente.
	 * Caso o usuário não exista, lança NotFoundException.
	 */
	@Override
	@Transactional
	public void deleteAddress(@NotNull Long userId) throws NotFoundException {

		User existingUser = checkUser(userId);
		Address existingAddress = addressRepository.findByUser(existingUser);

		if(existingAddress != null ) {
			existingUser.setAddress(null); // Desassocia endereço de usuário
			addressRepository.delete(existingAddress); // Exclui o endereço
		} else
			throw new NotFoundException();
	}

	// Verifica existência de usuário para associar endereço.
	@Transactional(readOnly = true)
	private User checkUser(Long userId) throws NotFoundException {

		Optional<User> optionalUser = userRepository.findById(userId);

		if(optionalUser.isPresent())
			return optionalUser.get();
		else
			throw new NotFoundException();
	}

	// verifica a consistência dos dados do endereço
	private boolean checkAddressState(AddressDTO addressDTO) {

		if(addressDTO.getPublicPlace() == null || addressDTO.getStreetAddress() == null
				|| addressDTO.getCity() == null || addressDTO.getState() == null || addressDTO.getZipCode() == null)
			return false;

		return true;
	}

}
