package com.venturasistemoj.restapi.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * [EN] Interface that extends JpaRepository, provided by Spring Data JPA with commonly used data access methods for
 * the User entity. In addition to the inherited methods, the custom method <code>findByCpf</code> is responsible for
 * searching for a user based on their cpf in order to avoid duplicate registration.
 *
 * [PT] Interface que estende JpaRepository, fornecida pelo Spring Data JPA com métodos de acesso a dados comumente
 * usados para a entidade User. Além dos métodos herdados, o método personalizado <code>findByCpf</code> é responsável
 * por buscar um usuário com base em seu cpf a fim de evitar duplicidade no cadastro.
 *
 * @author Wilson Ventura
 * @since 2023
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByCpf(String cpf);

}
