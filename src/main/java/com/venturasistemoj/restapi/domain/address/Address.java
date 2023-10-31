package com.venturasistemoj.restapi.domain.address;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.venturasistemoj.restapi.domain.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Entity class for an Address.
 *
 * @author Wilson Ventura
 */

@Entity
@Table(name = "adresses")
@Data
public class Address {

	/**
	 * The <italic>zip code</italic> is the <bold>brazilian CEP</bold>.
	 */
	private static final String ZIP_CODE_REGEXP = "^[\\d]{2}\\.?[\\d]{3}\\-?[\\d]{3}$";
	private static final String ZIP_CODE_MESSAGE = "Invalid brazilian CEP!";

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
	 * <p><code>@OneToOne</code> indicates the one-to-one relationship with the <code>User</code> class.</p>
	 * <p><code>@JoinColumn(name = "user_id")</code> specifies the column in the <code>addresses/<code> table used as
	 * the foreign key to map the relationship.</p>
	 */
	@OneToOne
	@JoinColumn(name = "user_id") // foreign key
	@JsonBackReference
	User user;

}
