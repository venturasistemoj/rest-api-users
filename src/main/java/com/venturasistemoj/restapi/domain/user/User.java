package com.venturasistemoj.restapi.domain.user;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.venturasistemoj.restapi.domain.address.Address;
import com.venturasistemoj.restapi.domain.phone.PhoneNumber;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Entity class for an User.
 *
 * @author Wilson Ventura
 */

@Entity
@Table(name = "users")
@Data // Equivalent to @Getter, @Setter, @RequiredArgsConstructor and @ToString @EqualsAndHashCode (overridden here).
public class User {

	/**
	 * <p>Static fields of an <code>@Entity</code> are considered independent of the persistence context and are not
	 * affected by JPA annotations. Therefore, it is not necessary to mark these fields with <code>@Transient</code>,
	 * which is only applied to instance fields and does not affect static fields.</p>
	 */

	// No check digit
	private static final String CPF_REGEXP = "^[\\d]{3}\\.?[\\d]{3}\\.?[\\d]{3}\\-?[\\d]{2}$";
	private static final String CPF_MESSAGE = "CPF inválido!";

	// https://www.w3schools.blog/validate-email-regular-expression-regex-java
	private static final String EMAIL_REGEXP =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String EMAIL_MESSAGE = "E-mail inválido!";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@NotNull private String name;
	@NotNull private String surName;

	/**
	 * <p>The <code>JacksonConfig</code> class configures custom serializers and deserializers at application-level
	 * for the types <code>LocalDate, LocalTime and LocalDateTime</code> eliminating the need for <code>@JsonFormat</code>
	 * annotation on each entity field of the application that needs it.</p>
	 */

	//@JsonFormat(pattern = "dd/MM/yyyy")
	@NotNull
	private LocalDate birthDate;

	@NotNull
	@Pattern(regexp = CPF_REGEXP, message = CPF_MESSAGE)
	@Column(unique = true)
	private String cpf;

	@NotNull
	@Pattern(regexp = EMAIL_REGEXP, message = EMAIL_MESSAGE)
	private String email;


	/**
	 * <p><code>@OneToOne</code> indicates a one-to-one relationship between the <code>User</code> and
	 * <code>Address</code>. <code>mappedBy</code> indicates that the <code>address</code> property is mapped by
	 * <code>user</code> (owner of the relationship) in the <code>Address</code> class, this means that the adresses
	 * table has a column <code>user_id</code> that references the primary key of the users table.
	 * <code>cascade</code> defines that persistence operations performed on the <code>User</code> entity will be
	 * propagated to the <code>Address/<code> entity.</p>
	 */
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Address address;

	/**
	 * <p><code>@OneToMany</code> indicates a one-to-many relationship between the <code>User</code> and
	 * <code>PhoneNumber</code>. <code>mappedBy/<code> specifies that the <code>phones</code> property is mapped by
	 * <code>user</code> (owner of the relationship) in the <code>PhoneNumber</code> class, this means that the phones
	 * table has a column <code>user_id</code> that references the primary key of the users table.
	 * <code>cascade</code> defines that persistence operations performed on the <code>User</code> entity will be
	 * propagated to the <code>PhoneNumber/<code> entity.</p>
	 */
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Set<PhoneNumber> phones;

	/**
	 * <p>The <code>@JsonManagedReference</code> and <code>@JsonBackReference</code> annotations
	 * (in <code>Address.user</code> and <code>PhoneNumber.user</code>) are from the Jackson library, used for
	 * serialization and deserialization of Java objects to JSON. They are used to treat the bidirectional relationship
	 * between <code>User</code>, <code>PhoneNumber</code> and <code>Address</code> in order to avoid infinite looping
	 * in JSON serialization.</p>
	 */

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		User other = (User) obj;
		return Objects.equals(cpf, other.cpf) && Objects.equals(email, other.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cpf, email);
	}

}