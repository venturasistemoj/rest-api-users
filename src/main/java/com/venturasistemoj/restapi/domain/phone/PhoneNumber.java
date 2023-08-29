package com.venturasistemoj.restapi.domain.phone;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.venturasistemoj.restapi.domain.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Entity class for a Phone Number
 *
 * @author Wilson Ventura
 * @since 2023
 */

@Entity
@Table(name = "phones")
@Data
public class PhoneNumber {

	private static final String PHONE_REGEXP = "^\\(?[1-9]{2}\\)? ?(?:[2-8]|9[1-9])[0-9]{3}\\-?[0-9]{4}$";
	private static final String PHONE_MESSAGE = "Telefone inválido!";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long phoneId;

	private String type;

	@Pattern(regexp = PHONE_REGEXP, message = PHONE_MESSAGE)
	@NotNull private String number;

	/**
	 * <code>@ManyToOne</code> indica um relacionamento muitos-para-um com a entidade <code>User</code>.
	 * <code>@JoinColumn</code> especifica a coluna na tabela <code>phones</code> que armazena a chave estrangeira
	 * para a tabela <code>users</code>.
	 */
	@ManyToOne
	@JoinColumn(name = "user_id") // foreign key
	@JsonBackReference
	private User user;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		PhoneNumber other = (PhoneNumber) obj;
		return Objects.equals(number, other.number);
	}

	@Override
	public int hashCode() {
		return Objects.hash(number);
	}

}
