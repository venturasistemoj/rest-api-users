package com.venturasistemoj.restapi.domain;

import java.time.LocalDate;
import java.util.Objects;
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class for an User.
 *
 * @author Wilson Ventura
 * @since 2023
 */

/*
 * [EN] When serializing related Java objects to JSON format, a circular reference problem may occur, resulting in
 * an infinite loop. To deal with this problem, Spring uses the @JsonManagedReference and @JsonBackReference annotations,
 * provided by the Jackson library, which is widely used in the context of Spring for serializing and deserializing Java
 * objects to JSON.
 * These annotations are used to resolve circular reference issues when serializing related objects into
 * bidirectional relationships.
 * In a two-way relationship, where one entity is the "parent" and the other is the "child", the @JsonManagedReference
 * annotation is placed on the "parent" entity to indicate that the relationship is managed from that entity. It marks
 * the property representing the relationship to the "child" entity. Jackson serializes the "parent" entity normally,
 * but when it encounters the property marked with @JsonManagedReference, it avoids serializing the "child" entity to
 * avoid the infinite loop.
 * The @JsonBackReference annotation is placed on the "child" entity and marks the property that represents the
 * relationship with the "parent" entity. This annotation is to prevent recursive serialization of the bidirectional
 * relationship. When serializing a "parent" object to JSON, the "child" property will be included normally in the JSON
 * result, but "child" object serialization will be avoided to avoid infinite loop.
 *
 * [PT] Durante a serialização de objetos Java relacionados para formato JSON, pode ocorrer um problema de referência
 * circular, resultando em um loop infinito. Para lidar com esse problema, o Spring utiliza as anotações
 * @JsonManagedReference e @JsonBackReference, fornecidas pela biblioteca Jackson, amplamente utilizada no contexto do
 * Spring para serialização e desserialização de objetos Java para JSON.
 * Essas anotações são usadas para resolver problemas de referência circular durante a serialização de objetos
 * relacionados em relacionamentos bidirecionais.
 * Em um relacionamento bidirecional, onde uma entidade é o "pai" e a outra é a "filha", a anotação
 * @JsonManagedReference é colocada na entidade "pai" para indicar que o relacionamento é gerenciado a partir dessa
 * entidade. Ela marca a propriedade que representa a relação com a entidade "filha". O Jackson serializa a entidade
 * "pai" normalmente, mas quando encontra a propriedade marcada com @JsonManagedReference, evita serializar a entidade
 * "filha" para evitar o loop infinito.
 * A anotação @JsonBackReference é colocada na entidade "filha" e marca a propriedade que representa a relação com a
 * entidade "pai". Essa anotação serve para evitar a serialização recursiva do relacionamento bidirecional. Ao
 * serializar um objeto "pai" para JSON, a propriedade "filha" será incluída normalmente no resultado JSON, mas a
 * serialização do objeto "filha" será evitada para evitar o loop infinito.
 *
 * this
 * @JsonManagedReference
 * private Set<PhoneNumber> phones;
 *
 * PhoneNumber class
 * @JsonBackReference
 * private User user;
 *
 */

/* [EN] The JacksonConfig class configures custom serializers and deserializers at application-level for the types
 * LocalDate, LocalTime and LocalDateTime eliminating the need for @JsonFormat annotation on each entity field of
 * the application that needs it.
 *
 * >> Note that the following Lombok annotations need to be present
 * @NoArgsConstructor e @AllArgsConstructor
 *
 * [PT] A classe JacksonConfig configura serializadores e desserializadores personalizados para os tipos
 * LocalDate, LocalTime e LocalDateTime em nível de aplicação eliminando a necessidade da anotação @JsonFormat em cada
 * campo de entidade da aplicação que precisar dela.
 *
 * >> Note que as seguintes anotações do Lombok precisam estar presentes
 * @NoArgsConstructor e @AllArgsConstructor
 *
 */


@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	@Transient
	private static final String cpfRegEx = "^[\\d]{3}\\.?[\\d]{3}\\.?[\\d]{3}\\-?[\\d]{2}$";

	@Transient
	private static final String emailRegEx =
	"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	// https://www.w3schools.blog/validate-email-regular-expression-regex-java

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull private String name;
	@NotNull private String surName;

	@NotNull
	//@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate birthDate;

	@NotNull
	@Pattern(regexp = cpfRegEx, message = "CPF inválido!")
	private String cpf; //sem dígito verificador

	@NotNull
	@Pattern(regexp = emailRegEx, message = "E-mail inválido!")
	private String email;

	// 'cascade' define o comportamento de propagação das operações de persistência da entidade "pai" para a entidade "filha".
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id") //foreign key
	private Address address;

	// 'mappedBy' especifica o nome da propriedade na entidade relacionada que é dona da relação.
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Set<PhoneNumber> phones;

	@Override
	public int hashCode() {
		return Objects.hash(birthDate, cpf, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		User other = (User) obj;
		return Objects.equals(birthDate, other.birthDate) && Objects.equals(cpf, other.cpf)
				&& Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return String.format(
				"%nUser - id: %d, name: %s, surname: %s, birthDate %s, cpf: %s, e-mail: %s%n%sPhones - %s%n",
				getId(), getName(), getSurName(), getBirthDate(), getCpf(), getEmail(), getAddress(), getPhones()
				);
	}

}