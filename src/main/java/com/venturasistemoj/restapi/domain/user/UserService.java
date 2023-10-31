package com.venturasistemoj.restapi.domain.user;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.venturasistemoj.restapi.exceptions.IllegalUserStateException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Service interface to user business logic.
 *
 * @author Wilson Ventura
 */
public interface UserService {

	UserDTO createUser(@Valid UserDTO userDTO) throws IllegalArgumentException, IllegalUserStateException;

	UserDTO updateUser(@NotNull Long userId, @Valid UserDTO userDTO)
			throws NotFoundException, IllegalArgumentException, IllegalUserStateException;

	UserDTO getUserById(@NotNull Long userId) throws NotFoundException;

	List<UserDTO> getUsers() throws NotFoundException;

	void deleteUser(@NotNull Long userId) throws NotFoundException;
}
