package com.venturasistemoj.restapi.domain.user;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.venturasistemoj.restapi.domain.address.AddressDTO;
import com.venturasistemoj.restapi.domain.phone.PhoneNumberDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [EN] User data transfer class
 * [PT] Classe de tranferência de dados de usuário.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

	@NotNull private Long userId;
	@NotNull private String name;
	@NotNull private String surName;
	@NotNull private LocalDate birthDate;
	@NotNull private String cpf;
	@NotNull private String email;

	@JsonManagedReference
	private AddressDTO addressDTO;

	@JsonManagedReference
	private List<PhoneNumberDTO> phonesDTO;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		UserDTO other = (UserDTO) obj;
		return Objects.equals(cpf, other.cpf) && Objects.equals(email, other.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cpf, email);
	}

	@Override
	public String toString() {
		return "UserDTO [userId=" + userId + ", name=" + name + ", surName=" + surName + ", birthDate=" + birthDate
				+ ", cpf=" + cpf + ", email=" + email + ", addressDTO=" + addressDTO + ", phonesDTO=" + phonesDTO + "]";
	}

}
