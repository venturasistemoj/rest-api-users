package com.venturasistemoj.restapi.domain.address;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.venturasistemoj.restapi.domain.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [EN] Address data transfer class
 * [PT] Classe de tranferência de dados de endereço.
 *
 * @author Wilson Ventura
 * @since 2023
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
}
