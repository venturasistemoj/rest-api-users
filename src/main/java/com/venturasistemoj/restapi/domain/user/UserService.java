package com.venturasistemoj.restapi.domain.user;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * [EN] Service interface to user business logic.
 *
 * [PT] Interface de serviços para lógica de negócios de usuários.
 *
 * @author Wilson Ventura
 * @since 2023
 */

public interface UserService {

	UserDTO createUser(@Valid UserDTO userDTO) throws IllegalArgumentException, IllegalUserStateException;

	UserDTO updateUser(@NotNull Long userId, @Valid UserDTO userDTO)
			throws NotFoundException, IllegalArgumentException, IllegalUserStateException;

	UserDTO getUserById(@NotNull Long userId) throws NotFoundException;

	List<UserDTO> getUsers() throws NotFoundException;

	void deleteUser(@NotNull Long userId) throws NotFoundException;
}
