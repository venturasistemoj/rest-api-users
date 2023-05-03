package com.venturasistemoj.restapi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.venturasistemoj.restapi.domain.User;
import com.venturasistemoj.restapi.domain.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * API controller class.
 *
 * @author Wilson Ventura
 * @since 2023
 */

/*
 * @CrossOrigin(origins = "*")
 * Anotação para permitir requisições de origem cruzada em classes de manipulador específicas e/ou métodos de manipulador.
 * Processada se um HandlerMapping apropriado estiver configurado. O Spring Web MVC e o Spring WebFlux suportam essa
 * anotação por meio do RequestMappingHandlerMapping em seus respectivos módulos. Os valores de cada tipo e par de
 * anotações de nível de método são adicionados a uma CorsConfiguration e, em seguida, os valores padrão são aplicados
 * por meio de CorsConfiguration.applyPermitDefaultValues().
 * As regras para combinar configuração global e local são geralmente aditivas - p. ex., todas as origens globais e locais.
 * Para aqueles atributos em que apenas um único valor pode ser aceito, como allowCredentials e maxAge, o local substitui
 * o valor global. Consulte CorsConfiguration.combine(CorsConfiguration) para obter mais detalhes. */

@RestController
@RequestMapping("/rest-api/users")
@Api(value = "Users Rest API")
@CrossOrigin(origins = "*")
public class UserController {

	private Optional<User> optionalUser;

	@Autowired
	private UserRepository userRepository;

	@PostMapping
	@ApiOperation(value = "Create a new user.")
	public ResponseEntity<User> createUser(@RequestBody User user) {

		final User savedUser = userRepository.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedUser); // 201
	}

	@PutMapping("/{id}")
	@ApiOperation(value = "Update an existing user.")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {

		optionalUser = userRepository.findById(id);

		if (optionalUser.isPresent() == false)
			return ResponseEntity.notFound().build(); // 404

		final User updatedUser = optionalUser.get();
		updatedUser.setName(user.getName());
		updatedUser.setBirthDate(user.getBirthDate());
		updatedUser.setCpf(user.getCpf());
		updatedUser.setEmail(user.getEmail());
		updatedUser.setAddress(user.getAddress());
		updatedUser.setPhones(user.getPhones());

		return ResponseEntity.ok(userRepository.save(updatedUser)); // 200
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Retrieve an existing user.")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {

		optionalUser = userRepository.findById(id);

		if (optionalUser.isPresent() == false)
			return ResponseEntity.notFound().build(); // 404

		return ResponseEntity.ok(optionalUser.get()); // 200
	}

	@GetMapping
	@ApiOperation(value = "Get a list of existing users.")
	public ResponseEntity<List<User>> getUsers() {

		final List<User> userlist = userRepository.findAll();

		if (userlist.isEmpty())
			return ResponseEntity.notFound().build(); // 404

		return ResponseEntity.ok(userlist); // 200
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "Delete an existing user.")
	public ResponseEntity<User> deleteUser(@PathVariable Long id) {

		optionalUser = userRepository.findById(id);

		if (optionalUser.isPresent() == false)
			return ResponseEntity.notFound().build(); // 404

		userRepository.delete(optionalUser.get());

		return ResponseEntity.noContent().build(); // 204
	}

}
