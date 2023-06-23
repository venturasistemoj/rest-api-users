package com.venturasistemoj.restapi.domain.phone;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.venturasistemoj.restapi.domain.user.User;

/**
 * [EN] Interface that extends JpaRepository, provided by Spring Data JPA with commonly used data access methods for
 * the PhoneNumber entity. In addition to the inherited methods, the custom method <code>findAllByUser/<code> is
 * responsible for fetching a phone list based on the associated user.
 *
 * [PT] Interface que estende JpaRepository, fornecida pelo Spring Data JPA com métodos de acesso a dados comumente
 * usados para a entidade PhoneNumber. Além dos métodos herdados, o método personalizado <code>findAllByUser</code>
 * é responsável por buscar a lista de telefones com base no usuário associado.
 *
 * @author Wilson Ventura
 * @since 2023
 */
public interface PhoneRepository extends JpaRepository<PhoneNumber, Long> {

	List<PhoneNumber> findAllByUser(User user);
}
