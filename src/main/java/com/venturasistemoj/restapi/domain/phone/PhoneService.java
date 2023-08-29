package com.venturasistemoj.restapi.domain.phone;

import java.util.Set;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

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

	Set<PhoneNumberDTO> updatePhoneNumber(@NotNull Long userId,  @Valid PhoneNumberDTO phonesDTO)
			throws NotFoundException, IllegalPhoneStateException;

	Set<PhoneNumberDTO> getPhonesByUserId(@NotNull Long userId) throws NotFoundException;

	Set<PhoneNumberDTO> getPhoneNumbers() throws NotFoundException;

	void deletePhoneNumber(@NotNull Long userId, @Valid PhoneNumberDTO phoneDTO)
			throws NotFoundException, IllegalPhoneStateException;
}
