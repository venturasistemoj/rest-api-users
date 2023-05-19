package com.venturasistemoj.restapi.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class for a Phone Number
 *
 * @author Wilson Ventura
 * @since 2023
 */

@Entity
@Table(name = "phones")
@Setter
@Getter
@NoArgsConstructor
public class PhoneNumber {

	@Transient
	private static final String phoneRegEx = "^\\(?[1-9]{2}\\)? ?(?:[2-8]|9[1-9])[0-9]{3}\\-?[0-9]{4}$";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "phone_id")
	private Long id;

	private String type;

	@Pattern(regexp = phoneRegEx, message = "Telefone inválido!")
	private String number;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id") // foreign key
	@JsonBackReference
	private User user;

	// https://projectlombok.org/features/Data
	@lombok.experimental.Tolerate
	public PhoneNumber(String type, @Pattern(regexp = phoneRegEx, message = "Telefone inválido!") String number) {
		super();
		this.type = type;
		this.number = number;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, number, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		PhoneNumber other = (PhoneNumber) obj;
		return Objects.equals(id, other.id) && Objects.equals(number, other.number) && Objects.equals(type, other.type);
	}

	@Override
	public String toString() {
		return String.format("PhoneNumber: id: %d, type: %s, number: %s ", getId(), getType(), getNumber());
	}

}
