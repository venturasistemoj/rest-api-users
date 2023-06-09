package com.venturasistemoj.restapi.domain.phone;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.venturasistemoj.restapi.domain.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [EN] Phone number data transfer class.
 * [PT] Classe de tranferência de dados de telefone.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneNumberDTO {

	@NotNull private Long phoneId;
	private String type;
	@NotNull private String number;

	@JsonBackReference
	@NotNull
	private UserDTO userDTO;

}
