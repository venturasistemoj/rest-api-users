package com.venturasistemoj.restapi.domain.address;

import java.util.List;
import java.util.Optional;

import com.venturasistemoj.restapi.domain.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.venturasistemoj.restapi.domain.user.User;
import com.venturasistemoj.restapi.domain.user.UserMapper;
import com.venturasistemoj.restapi.domain.user.UserRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * [EN] Class implementing the AddressService interface for managing business logic related to adresses.
 * <p>
 * [PT] Classe de implementação da interface AddressService para gerenciar a lógica de negócios relacionada a endereços.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    private static final String INCONPLETE_ADDRESS_DATA = "Dados do endereço incompletos!";

    /**
     * Cria um novo endereço para um usuário existente.
     * Caso o usuário não exista, lança NotFoundException.
     * Caso os dados do endereço estejam corrompidos ou exista endereço cadastrado, lança IllegalArgumentException.
     * RN3: não é possível criar endereço sem usuário.
     * RN4: não é possível criar mais de um endereço para um usuário.
     */
    @Override
    public AddressDTO createAddress(@NotNull Long userId, @Valid AddressDTO addressDTO)
            throws NotFoundException, IllegalStateException, IllegalAddressStateException {

        User existingUser = checkUser(userId); // RN3

        if (existingUser.getAddress() != null)
            throw new IllegalStateException(); // RN4

        if (!addressDTO.isValid())
            throw new IllegalAddressStateException(INCONPLETE_ADDRESS_DATA);

        UserDTO userDTO = userMapper.userToUserDTO(existingUser);

        AddressDTO updatedAddressDTO = new AddressDTO(
                addressDTO.addressId(),
                addressDTO.publicPlace(),
                addressDTO.streetAddress(),
                addressDTO.complement(),
                addressDTO.city(),
                addressDTO.state(),
                addressDTO.zipCode(),
                userDTO
        );
        Address savedAddress = addressRepository.save(addressMapper.addressDTOToAddress(updatedAddressDTO));
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

        if (existingAddress == null)
            throw new NotFoundException();

        if (!addressDTO.isValid())
            throw new IllegalAddressStateException(INCONPLETE_ADDRESS_DATA);

        if (existingAddress.equals(addressMapper.addressDTOToAddress(addressDTO)))
            throw new IllegalArgumentException();

        UserDTO userDTO = userMapper.userToUserDTO(existingUser);

        AddressDTO updatedAddressDTO = new AddressDTO(
                existingAddress.getAddressId(),
                existingAddress.getPublicPlace(),
                existingAddress.getStreetAddress(),
                existingAddress.getComplement(),
                existingAddress.getCity(),
                existingAddress.getState(),
                existingAddress.getZipCode(),
                userDTO // Set the UserDTO here
        );

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

        if (existingUser.getAddress() != null)
            return addressMapper.addressToAddressDTO(addressRepository.findByUser(existingUser));
        else
            throw new NotFoundException();
    }

    /**
     * Obtém todos os endereços da base de dados.
     *
     * @throws NotFoundException
     */
    @Override
    @Transactional(readOnly = true)
    public List<AddressDTO> getAdresses() throws NotFoundException {

        if (addressRepository.findAll().isEmpty())
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

        if (existingAddress != null) {
            existingUser.setAddress(null); // Desassocia endereço de usuário
            addressRepository.delete(existingAddress); // Exclui o endereço
        } else
            throw new NotFoundException();
    }

    // Verifica existência de usuário para associar endereço.
    @Transactional(readOnly = true)
    protected User checkUser(Long userId) throws NotFoundException {

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent())
            return optionalUser.get();
        else
            throw new NotFoundException();
    }

}
