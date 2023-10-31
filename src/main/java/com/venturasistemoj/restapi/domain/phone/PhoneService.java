package com.venturasistemoj.restapi.domain.phone;

import java.util.Set;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.venturasistemoj.restapi.exceptions.IllegalPhoneStateException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Service interface to phone number business logic.
 *
 * @author Wilson Ventura
 */
public interface PhoneService {

	PhoneNumberDTO createPhoneNumber(@NotNull Long userId, @Valid PhoneNumberDTO phoneDTO)
			throws NotFoundException, IllegalPhoneStateException;

	Set<PhoneNumberDTO> updatePhoneNumber(@NotNull Long userId,  @Valid PhoneNumberDTO phonesDTO)
			throws NotFoundException, IllegalPhoneStateException;

	Set<PhoneNumberDTO> getPhonesByUserId(@NotNull Long userId) throws NotFoundException;

	Set<PhoneNumberDTO> getPhoneNumbers() throws NotFoundException;

	void deletePhoneNumber(@NotNull Long userId, @Valid PhoneNumberDTO phoneDTO)
			throws NotFoundException, IllegalPhoneStateException;
}
