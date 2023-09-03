package com.venturasistemoj.restapi.domain.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.venturasistemoj.restapi.domain.address.AddressDTO;
import com.venturasistemoj.restapi.domain.phone.PhoneNumberDTO;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;
import java.util.Set;

/**
 * [EN] User data transfer class
 * [PT] Classe de tranferência de dados de usuário.
 *
 * @author Wilson Ventura
 * @since 2023
 */

public record UserDTO(
		@NotNull Long userId,
		@NotNull String name,
		@NotNull String surName,
		@NotNull LocalDate birthDate,
		@NotNull String cpf,
		@NotNull String email,
		@JsonManagedReference AddressDTO addressDTO,
		@JsonManagedReference Set<PhoneNumberDTO> phonesDTO
) {
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

	public static UserDTO createSampleUser() {
		return new UserDTO(
				1L,
				"Dilma",
				"Rousseff",
				LocalDate.of(1956, Month.OCTOBER, 26),
				"789.456.123-10",
				"dilma@prov.com",
				null,
				null
		);
	}

	public static UserDTO createSampleMaleUser(){
		return new UserDTO(1L, "Luiz Inacio", "da Silva", LocalDate.of(1972, Month.FEBRUARY, 22), "123.456.789-10", "lula@prov.com", null, null);
	}

	public static UserDTO createSampleMaleUser(AddressDTO addressDTO){
		return new UserDTO(1L, "Luiz Inacio", "da Silva", LocalDate.of(1972, Month.FEBRUARY, 22), "123.456.789-10", "lula@prov.com", addressDTO, null);
	}

	public static UserDTO createSampleMaleUser(AddressDTO addressDTO, PhoneNumberDTO phoneNumberDTO){
		return new UserDTO(1L, "Luiz Inacio", "da Silva", LocalDate.of(1972, Month.FEBRUARY, 22), "123.456.789-10", "lula@prov.com", addressDTO, Set.of(phoneNumberDTO));
	}
}
