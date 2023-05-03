package com.venturasistemoj.restapi.domain;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity class for an User.
 *
 * @author Ventura
 * @since 2023
 */



/*
 * Relações bidirecionais no Jackson e "Jackson JSON recursão infinita".
 * https://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion
 *
 * @JsonManagedReference é a parte dianteira da referência, aquela que é serializada normalmente.
 * @JsonBackReference é a parte de trás da referência; ela será omitida da serialização.
 * Os objetos Set<Phonenunber> e Address serializados não contém uma referência ao objeto User.
 * Observe também que não podemos alternar as anotações, @JsonBackReference não pode ser usado em uma coleção.
 */

/*
 * Hibernate Error: Object References an Unsaved Transient Instance
 * https://www.baeldung.com/hibernate-unsaved-transient-instance-error
 *
 * this
 * @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
 *
 * class PhoneNumber
 * @Cascade(CascadeType.SAVE_UPDATE)
 * User user
 */

@Entity
@Table(name = "users")
@Setter
@Getter
@ToString
public class User {

	@Transient
	private static final String cpfRegEx = "^[\\d]{3}\\.?[\\d]{3}\\.?[\\d]{3}\\-?[\\d]{2}$";

	@Transient
	private static final String emailRegEx = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	// https://www.w3schools.blog/validate-email-regular-expression-regex-java

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;

	@NotNull private String name;
	@NotNull private String surName;
	@NotNull private LocalDate birthDate;

	@NotNull
	@Pattern(regexp = cpfRegEx, message = "CPF inválido!")
	private String cpf; //sem dígito verificador

	@NotNull
	@Pattern(regexp = emailRegEx, message = "E-mail inválido!")
	private String email;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "addressId", nullable = false)
	@JsonManagedReference
	private Address address;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private Set<PhoneNumber> phones;

	// https://projectlombok.org/features/Data
	@lombok.experimental.Tolerate
	public User(@NotNull String name, @NotNull String surName, @NotNull LocalDate birthDate,
			@NotNull @Pattern(regexp = cpfRegEx, message = "CPF inválido!") String cpf,
			@NotNull @Pattern(regexp = emailRegEx, message = "E-mail inválido!") String email) {
		super();
		this.name = name;
		this.surName = surName;
		this.birthDate = birthDate;
		this.cpf = cpf;
		this.email = email;
	}

}