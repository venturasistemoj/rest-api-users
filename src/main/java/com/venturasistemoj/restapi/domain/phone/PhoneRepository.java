package com.venturasistemoj.restapi.domain.phone;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.venturasistemoj.restapi.domain.user.User;

/**
 * <p>Interface that extends <code>JpaRepository</code>, provided by Spring Data JPA with commonly used data access
 * methods for the <code>PhoneNumber</code> entity. In addition to the inherited methods, the custom method
 * <code>findAllByUser/<code> is responsible for fetching a phone set based on the associated user.</p>
 *
 * @author Wilson Ventura
 */
public interface PhoneRepository extends JpaRepository<PhoneNumber, Long> {

	Set<PhoneNumber> findAllByUser(User user);
}
