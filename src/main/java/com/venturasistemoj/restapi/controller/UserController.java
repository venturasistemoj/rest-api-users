package com.venturasistemoj.restapi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.venturasistemoj.restapi.domain.PhoneNumber;
import com.venturasistemoj.restapi.domain.User;
import com.venturasistemoj.restapi.domain.UserRepository;

/**
 * API controller class.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@RestController
@RequestMapping("/rest-api/users")
public class UserController {

	private Optional<User> optionalUser;

	@Autowired
	private UserRepository userRepository;

	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user) {

		final User savedUser = userRepository.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedUser); // 201
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {

		Optional<User> optionalUser = userRepository.findById(id);

		if (optionalUser.isPresent()) {
			User existingUser = optionalUser.get();

			// Verifica se o id fornecido na requisição corresponde ao id do usuário existente
			if ( ! existingUser.getId().equals(user.getId()))
				return ResponseEntity.badRequest().build(); // 400 - Id inválido

			// Atualiza o endereço
			if (user.getAddress() != null)
				existingUser.setAddress(user.getAddress());

			// Atualiza os números de telefone
			if (user.getPhones() != null) {
				existingUser.getPhones().clear();
				existingUser.getPhones().addAll(user.getPhones());
				for (PhoneNumber phoneNumber : existingUser.getPhones())
					phoneNumber.setUser(existingUser);
			}

			User updatedUser = userRepository.save(existingUser);
			return ResponseEntity.ok(updatedUser); // 200 - Usuário atualizado

		} else
			return ResponseEntity.notFound().build(); // 404
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {

		optionalUser = userRepository.findById(id);

		if (optionalUser.isPresent() == false)
			return ResponseEntity.notFound().build(); // 404

		return ResponseEntity.ok(optionalUser.get()); // 200
	}

	@GetMapping
	public ResponseEntity<List<User>> getUsers() {

		final List<User> userlist = userRepository.findAll();

		if (userlist.isEmpty())
			return ResponseEntity.notFound().build(); // 404

		return ResponseEntity.ok(userlist); // 200
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable Long id) {

		optionalUser = userRepository.findById(id);

		if (optionalUser.isPresent() == false)
			return ResponseEntity.notFound().build(); // 404

		userRepository.delete(optionalUser.get());

		return ResponseEntity.noContent().build(); // 204
	}

}
