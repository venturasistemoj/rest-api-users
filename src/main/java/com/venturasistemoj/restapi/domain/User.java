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
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity class for an User.
 *
 * @author Wilson Ventura
 * @since 2023
 */

/*
 * [EN] Bidirectional relationships in Jackson
 * Jackson JSON infinite recursion problem
 * For two entities with a simple one-to-many relationship, when we try to serialize an instance of one entity Jackson
 * throws a JsonMappingException exception. So we annotate the relationships with @JsonManagedReference, and
 * @JsonBackReference to allow Jackson to better handle the relation.
 *
 * @JsonManagedReference is the forward part of reference, the one that gets serialized normally.
 * @JsonBackReference is the back part of reference; it'll be omitted from serialization.
 * Also note that we can't switch around the annotations.
 *
 * [PT] Relacionamentos bidirecionais no Jackson
 * O Problema de recursão infinita JSON Jackson
 * Para duas entidades com um relacionamento simples um-para-muitos, quando tentamos serializar uma instância de uma
 * entidade, o Jackson lança uma exceção JsonMappingException. Dessa forma, devemos anotar os relacionamentos com
 * @JsonManagedReference e @JsonBackReference para permitir que Jackson lide melhor com a relação.
 *
 * @JsonManagedReference é a parte dianteira da referência, aquela que é serializada normalmente.
 * @JsonBackReference é a parte traseira da referência;  será omitida da serialização.
 * Observe também que não podemos alternar entre as anotações.
 */

/*
 * [EN] Hibernate Error: Object References an Unsaved Transient Instance
 * This error occurs from the Hibernate session when we try to persist a managed entity, and that entity references
 * an unsaved transient instance. A solution to cascade save/update/delete operations for entity relationships that
 * depend on the existence of another entity is using a proper CascadeType in the entity associations.
 *
 * [PT] Erro Hibernate: o objeto faz referência a uma instância transiente não salva
 * Esse erro ocorre na sessão do Hibernate quando tentamos persistir uma entidade gerenciada que faz referência a
 * uma instância transiente não salva. Uma solução para salvar/atualizar/excluir operações em cascata para
 * relacionamentos de entidade que dependem da existência de outra entidade é usar um CascadeType adequado nas
 * associações de entidade.
 *
 * @OneToOne
 *
 * In this class / Nesta classe
 * @OneToOne(cascade = CascadeType.ALL)
 *
 * In the Address class / Na classe Address
 * @OneToOne(mappedBy = "address")
 *
 * @OneToMany and @ManyToOne
 *
 * In this class / Nesta classe
 * @OneToMany(cascade = CascadeType.ALL)
 *
 * In the PhoneNumber class / Na classe PhoneNumber
 * @Cascade(CascadeType.SAVE_UPDATE)
 *
 */

@Entity
@Table(name = "users")
@NoArgsConstructor
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

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
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