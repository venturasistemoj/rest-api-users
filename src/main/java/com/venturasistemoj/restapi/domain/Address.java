package com.venturasistemoj.restapi.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class for an Address.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@Entity
@Table(name = "addresses")
@NoArgsConstructor
@Setter
@Getter
public class Address {

	@Transient
	private static final String zipCodeRegEx = "^[\\d]{2}\\.?[\\d]{3}\\-?[\\d]{3}$";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String publicPlace;
	private String streetAddress;
	private String complement;
	private String city;
	private String state;

	@Pattern(regexp = zipCodeRegEx, message = "CEP inválido!")
	private String zipCode;

	// https://projectlombok.org/features/Data
	@lombok.experimental.Tolerate
	public Address(String publicPlace, String streetAddress, String complement, String city, String state,
			@Pattern(regexp = zipCodeRegEx, message = "CEP inválido!") String zipCode) {
		super();
		this.publicPlace = publicPlace;
		this.streetAddress = streetAddress;
		this.complement = complement;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, publicPlace, streetAddress, zipCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		Address other = (Address) obj;
		return Objects.equals(id, other.id) && Objects.equals(publicPlace, other.publicPlace)
				&& Objects.equals(streetAddress, other.streetAddress) && Objects.equals(zipCode, other.zipCode);
	}

	@Override
	public String toString() {
		return String.format("Address - id: %d, publicPlace: %s, streetAddress: %s, complement: %s, city: %s, state: %s%n",
				getId(), getPublicPlace(), getStreetAddress(), getComplement(), getCity(), getState()
				);
	}

}
