package com.venturasistemoj.restapi.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity class for an Address.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@Entity
@Table(name = "addresses")
@Setter
@Getter
@ToString
public class Address {

	@Transient
	private static final String zipCodeRegEx = "^[\\d]{2}\\.?[\\d]{3}\\-?[\\d]{3}$";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long addressId;

	private String publicPlace;
	private String streetAddress;
	private String complement;
	private String city;
	private String state;

	@Pattern(regexp = zipCodeRegEx, message = "CEP inválido!")
	private String zipCode;

	@OneToOne(mappedBy = "address", optional = false)
	@JsonBackReference
	private User user;

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

}
