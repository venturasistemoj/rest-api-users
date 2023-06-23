package com.venturasistemoj.restapi.domain.address;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.venturasistemoj.restapi.domain.user.User;

import lombok.Data;

/**
 * Entity class for an Address.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@Entity
@Table(name = "addresses")
@Data
public class Address {

	private static final String ZIP_CODE_REGEXP = "^[\\d]{2}\\.?[\\d]{3}\\-?[\\d]{3}$";
	private static final String ZIP_CODE_MESSAGE = "CEP inválido!";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addressId;

	@NotNull private String publicPlace;
	@NotNull private String streetAddress;
	private String complement;
	@NotNull private String city;
	@NotNull private String state;

	@Pattern(regexp = ZIP_CODE_REGEXP, message = ZIP_CODE_MESSAGE)
	@NotNull private String zipCode;

	/**
	 * <code>@OneToOne</code> indica a relação de um-para-um com a classe <code>User</code>.
	 * <code>@JoinColumn(name = "user_id")</code> especifica a coluna na tabela <code>adresses/<code> usada como chave
	 * estrangeira para mapear a relação.
	 */
	@OneToOne
	@JoinColumn(name = "user_id") // foreign key
	@JsonBackReference
	User user;

}
