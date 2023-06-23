package com.venturasistemoj.restapi.domain.phone;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

/**
 * [EN] Service interface to phone number business logic.
 *
 * [PT] Interface de serviços para lógica de negócios de telefones.
 *
 * @author Wilson Ventura
 * @since 2023
 */
public interface PhoneService {

	PhoneNumberDTO createPhoneNumber(@NotNull Long userId, @Valid PhoneNumberDTO phoneDTO)
			throws NotFoundException, IllegalStateException, IllegalPhoneStateException;

	List<PhoneNumberDTO> updatePhoneNumber(@NotNull Long userId,  @Valid PhoneNumberDTO phonesDTO)
			throws NotFoundException, IllegalPhoneStateException;

	List<PhoneNumberDTO> getPhonesByUserId(@NotNull Long userId) throws NotFoundException;

	List<PhoneNumberDTO> getPhoneNumbers() throws NotFoundException;

	void deletePhoneNumber(@NotNull Long userId, @Valid PhoneNumberDTO phoneDTO)
			throws NotFoundException, IllegalPhoneStateException;
}
