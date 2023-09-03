package com.venturasistemoj.restapi.domain.phone;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.venturasistemoj.restapi.domain.address.AddressDTO;
import com.venturasistemoj.restapi.domain.user.User;
import com.venturasistemoj.restapi.domain.user.UserDTO;

import jakarta.validation.constraints.NotNull;

/**
 * [EN] Phone number data transfer class.
 * [PT] Classe de tranferência de dados de telefone.
 *
 * @author Wilson Ventura
 * @since 2023
 */

public record PhoneNumberDTO(
		@NotNull Long phoneId,
		String type,
		@NotNull String number,
		@JsonBackReference @NotNull UserDTO userDTO
) {
	public PhoneNumberDTO withUserDTO(UserDTO userDTO) {
		return new PhoneNumberDTO(phoneId, type, number, userDTO);
	}

	public boolean isValid() {
		// Check for non-null values and other validation criteria
		return phoneId() != null &&
				type() != null && !type().isEmpty() &&
				number() != null && !number().isEmpty();
	}

	public static PhoneNumberDTO createSamplePhoneNumber(UserDTO userDTO) {
		return new PhoneNumberDTO(1L, "Cel", "(21) 96687-8776", userDTO);
	}

	public static PhoneNumberDTO createSamplePhoneNumber() {
		return new PhoneNumberDTO(1L, "Cel", "(21) 96687-8776", null);
	}
}
