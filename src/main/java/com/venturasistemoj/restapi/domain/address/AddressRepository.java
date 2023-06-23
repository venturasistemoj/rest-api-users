package com.venturasistemoj.restapi.domain.address;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import com.venturasistemoj.restapi.domain.user.User;

/**
 * [EN] Interface that extends JpaRepository, provided by Spring Data JPA with commonly used data access methods for
 * the Address entity. In addition to the inherited methods, the <code>findByUser</code> custom method is responsible
 * for fetching an address based on the user associated with it.
 *
 * [PT] Interface que estende JpaRepository, fornecida pelo Spring Data JPA com métodos de acesso a dados comumente
 * usados para a entidade Address. Além dos métodos herdados, o método personalizado <code>findByUser</code> é
 * responsável por buscar um endereço com base no usuário associado a ele.
 *
 * @author Wilson Ventura
 * @since 2023
 */
public interface AddressRepository extends JpaRepository<Address, Long> {

	Address findByUser(User user) throws NotFoundException;
}
