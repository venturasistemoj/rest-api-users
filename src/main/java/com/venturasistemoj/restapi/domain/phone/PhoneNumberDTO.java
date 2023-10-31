package com.venturasistemoj.restapi.domain.phone;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.venturasistemoj.restapi.domain.user.UserDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Phone number data transfer class.
 *
 * @author Wilson Ventura
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneNumberDTO {

	@NotNull private Long phoneId;
	@NotNull private String type;
	@NotNull private String number;

	@JsonBackReference
	@NotNull
	private UserDTO userDTO;

	@Override
	public String toString() {
		return "PhoneNumberDTO [phoneId=" + phoneId + ", type=" + type + ", number=" + number + "]";
	}

}
