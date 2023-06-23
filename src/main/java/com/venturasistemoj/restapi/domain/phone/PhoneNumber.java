package com.venturasistemoj.restapi.domain.phone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.venturasistemoj.restapi.domain.user.User;

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
	@Column(name = "phone_id")
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

}
