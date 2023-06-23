package com.venturasistemoj.restapi.domain.address;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

/**
 * [EN] Service interface to address business logic.
 *
 * [PT] Interface de serviços para lógica de negócios de endereços.
 *
 * @author Wilson Ventura
 * @since 2023
 */
public interface AddressService {

	AddressDTO createAddress(@NotNull Long userId, @Valid AddressDTO addressDTO)
			throws IllegalAddressStateException, IllegalStateException, NotFoundException;

	AddressDTO updateAddress(@NotNull Long userId, @Valid AddressDTO addressDTO)
			throws NotFoundException, IllegalArgumentException, IllegalAddressStateException;

	AddressDTO getAddressByUserId(@NotNull Long userId) throws NotFoundException;

	List<AddressDTO> getAdresses() throws NotFoundException;

	void deleteAddress(@NotNull Long userId) throws NotFoundException;

}
