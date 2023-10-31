package com.venturasistemoj.restapi.domain.address;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.venturasistemoj.restapi.exceptions.IllegalAddressStateException;
import com.venturasistemoj.restapi.exceptions.IllegalOperationException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Service interface to address business logic.
 *
 * @author Wilson Ventura
 */
public interface AddressService {

	AddressDTO createAddress(@NotNull Long userId, @Valid AddressDTO addressDTO)
			throws NotFoundException, IllegalOperationException, IllegalAddressStateException;

	AddressDTO updateAddress(@NotNull Long userId, @Valid AddressDTO addressDTO)
			throws NotFoundException, IllegalAddressStateException;

	AddressDTO getAddressByUserId(@NotNull Long userId) throws NotFoundException;

	List<AddressDTO> getAdresses() throws NotFoundException;

	void deleteAddress(@NotNull Long userId) throws NotFoundException;

}
