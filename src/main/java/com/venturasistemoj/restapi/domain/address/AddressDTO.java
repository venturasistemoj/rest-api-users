package com.venturasistemoj.restapi.domain.address;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.venturasistemoj.restapi.domain.user.UserDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Address data transfer class.
 *
 * @author Wilson Ventura
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {

	@NotNull private Long addressId;
	@NotNull private String publicPlace;
	@NotNull private String streetAddress;
	private String complement;
	@NotNull private String city;
	@NotNull private String state;
	@NotNull private String zipCode;

	@JsonBackReference
	@NotNull
	private UserDTO userDTO;

	@Override
	public String toString() {
		return "AddressDTO [addressId=" + addressId + ", publicPlace=" + publicPlace + ", streetAddress="
				+ streetAddress + ", complement=" + complement + ", city=" + city + ", state=" + state + ", zipCode="
				+ zipCode + "]";
	}

}
