package com.venturasistemoj.restapi.domain.address;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import com.venturasistemoj.restapi.domain.user.User;

/**
 * <p>Interface that extends <code>JpaRepository</code>, provided by Spring Data JPA with commonly used data access
 * methods for the <code>Address</code> entity. In addition to the inherited methods, the <code>findByUser</code> custom
 * method is responsible for fetching an address based on the user associated with it.</p>
 *
 * @author Wilson Ventura
 */

public interface AddressRepository extends JpaRepository<Address, Long> {

	Address findByUser(User user) throws NotFoundException;
}
