package com.venturasistemoj.restapi.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [EN] Users API interface for coordinating requests and responses.
 *
 * [PT] Interface da API de usuários para coordenar requisições e respostas.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@RestController
@RequestMapping("/rest-api/users")
public class UserController {

	@Autowired
	private UserService userService;

	private static final String USER_NOT_FOUND = "Usuário(s) inexistente(s)!";
	private static final String USER_CPF = "Usuário já possui cpf cadastrado!";

	@PostMapping
	public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {

		try {
			UserDTO savedUser = userService.createUser(userDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
		} catch (IllegalUserStateException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(USER_CPF);
		}
	}

	@PutMapping("/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {

		try {
			UserDTO	updatedUser = userService.updateUser(userId, userDTO);
			return ResponseEntity.ok(updatedUser);
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
		} catch (IllegalUserStateException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(USER_CPF);
		}
	}

	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserById(@PathVariable Long userId) {

		try {
			UserDTO	existingUser = userService.getUserById(userId);
			return ResponseEntity.ok(existingUser);
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
		}
	}

	@GetMapping
	public ResponseEntity<?> getUsers() {

		try {
			return ResponseEntity.ok(userService.getUsers());
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
		}
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable Long userId) {

		try {
			userService.deleteUser(userId);
			return ResponseEntity.noContent().build();
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
		}
	}

}
