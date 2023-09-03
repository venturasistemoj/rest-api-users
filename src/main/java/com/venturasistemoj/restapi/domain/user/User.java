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

/**
 * Entity class for an User.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@Entity
@Table(name = "users")
public class User {

	// Sem dígito verificador
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
	 * [EN] The <code>JacksonConfig2</code> class configures custom serializers and deserializers at application-level
	 * for the types <code>LocalDate, LocalTime and LocalDateTime</code> eliminating the need for <code>@JsonFormat</code>
	 * annotation on each entity field of the application that needs it.
	 *
	 * [PT] A classe <code>JacksonConfig2</code> configura serializadores e desserializadores personalizados para os tipos
	 * <code>LocalDate, LocalTime e LocalDateTime</code> em nível de aplicação eliminando a necessidade da anotação
	 * <code>@JsonFormat</code> em cada campo de entidade da aplicação que precisar dela.
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
	 * <code>@OneToOne</code> indica um relacionamento um-para-um entre as entidades <code>User</code> e
	 * <code>Address.</code> <code>mappedBy</code> indica que a propriedade <code>address</code> é mapeada por
	 * <code>user</code> (dona da relação) na classe <code>Address</code>, isso significa que a tabela adresses
	 * possui uma coluna <code>user_id</code> que faz referência à chave primária da tabela users.
	 * <code>cascade</code> define que as operações de persistência realizadas na entidade <code>User</code> serão
	 * propagadas para a entidade <code>Address/<code>.
	 */
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Address address;

	/**
	 * <code>@OneToMany</code> indica um relacionamento um-para-muitos entre a entidade <code>User</code> e a entidade
	 * <code>PhoneNumber</code>. <code>mappedBy/<code> especifica que a propriedade <code>phones</code> é mapeada por
	 * <code>user</code> (dona da relação) na classe <code>PhoneNumber</code>, isso significa que a tabela phones
	 * possui uma coluna <code>user_id</code> que faz referência à chave primária da tabela users.
	 */
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Set<PhoneNumber> phones;

	/**
	 * As anotações <code>@JsonManagedReference</code> e <code>@JsonBackReference</code>
	 * (em <code>Address.user</code> e <code>PhoneNumber.user</code>) são da biblioteca Jackson, usada para a
	 * serialização e desserialização de objetos Java para JSON. São utilizadas para tratar o relacionamento
	 * bidirecional entre <code>User</code>, <code>PhoneNumber</code> e <code>Address</code> a fim de evitar o loop
	 * infinito na serialização em JSON.
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Set<PhoneNumber> getPhones() {
		return phones;
	}

	public void setPhones(Set<PhoneNumber> phones) {
		this.phones = phones;
	}
}