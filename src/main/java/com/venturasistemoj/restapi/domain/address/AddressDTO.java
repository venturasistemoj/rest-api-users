package com.venturasistemoj.restapi.domain.address;

import com.venturasistemoj.restapi.domain.user.UserDTO;
import jakarta.validation.constraints.NotNull;

public record AddressDTO(
		@NotNull Long addressId,
		@NotNull String publicPlace,
		@NotNull String streetAddress,
		String complement,
		@NotNull String city,
		@NotNull String state,
		@NotNull String zipCode,
		@NotNull UserDTO userDTO
) {
	public static AddressDTO createSampleAddress(UserDTO userDTO) {
		return new AddressDTO(
				1L,
				"Avenida",
				"Glasshouse, 69",
				"1001",
				"Rio 40º",
				"RJ",
				"69.069-069",
				userDTO
		);
	}
	public static AddressDTO createSampleAddress() {
		return new AddressDTO(
				1L,
				"Avenida",
				"Glasshouse, 69",
				"1001",
				"Rio 40º",
				"RJ",
				"69.069-069",
				null
		);
	}

	public boolean isValid() {
		// Check for non-empty strings and validate zip code format
		return !publicPlace.isEmpty() &&
				!streetAddress.isEmpty() &&
				!city.isEmpty() &&
				!state.isEmpty() &&
				isValidZipCode(zipCode);
	}

	private boolean isValidZipCode(String zipCode) {
		return true; //todo
	}
}
