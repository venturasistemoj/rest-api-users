package com.venturasistemoj.restapi.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity class for a Phone Number
 *
 * @author Wilson Ventura
 * @since 2023
 */

@Entity
@Table(name = "phones")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class PhoneNumber {

	@Transient
	private static final String phoneRegEx = "^\\(?[1-9]{2}\\)? ?(?:[2-8]|9[1-9])[0-9]{3}\\-?[0-9]{4}$";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long phoneId;

	private String type;

	@Pattern(regexp = phoneRegEx, message = "Telefone inválido!")
	private String number;

	@ManyToOne
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "userId", nullable = false)
	@JsonBackReference
	private User user;

	// https://projectlombok.org/features/Data
	@lombok.experimental.Tolerate
	public PhoneNumber(String type, @Pattern(regexp = phoneRegEx, message = "Telefone inválido!") String number) {
		super();
		this.type = type;
		this.number = number;
	}

}
