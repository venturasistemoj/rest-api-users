package com.venturasistemoj.restapi.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>Interface that extends <code>JpaRepository</code>, provided by Spring Data JPA with commonly used data access
 * methods for the <code>User</code> entity. In addition to the inherited methods, the custom method <code>findByCpf</code>
 * is responsible for searching for a user based on their CPF in order to avoid duplicate registration.</p>
 *
 * @author Wilson Ventura
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByCpf(String cpf);

}
