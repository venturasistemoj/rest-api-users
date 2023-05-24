package com.venturasistemoj.restapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.venturasistemoj.restapi.controller.UserController;
import com.venturasistemoj.restapi.domain.Address;
import com.venturasistemoj.restapi.domain.PhoneNumber;
import com.venturasistemoj.restapi.domain.User;
import com.venturasistemoj.restapi.domain.UserRepository;

/**
 * [EN] Test class for UserController class with JUnit and Mockito (Without loading application context).
 * This claas uses the Mockito library to create UserRepository mocks and inject them into the UserController using
 * the @InjectMocks annotation. It also uses the @BeforeEach annotation to initialize the mocks before each test using
 * MockitoAnnotations.openMocks(this).
 *
 * On each test, configures the expected behavior of the mocks, then calls the methods of UserController and
 * checks the results. The when method activates fragmentation methods and is used for the simulation to return a value
 * specific when a specific method is called.
 *
 * [PT] Classe de teste com JUnit e Mockito (Sem carregar o contexto da aplicação).
 * Esta classe utiliza a biblioteca Mockito para criar mocks de UserRepository e os injeta no UserController com a
 * anotação @InjectMocks. Também utiliza a anotação @BeforeEach para inicializar os mocks antes de cada teste usando
 * MockitoAnnotations.openMocks(this).
 *
 * Em cada teste, configura o comportamento esperado dos mocks usando a função when e thenReturn do Mockito.
 * Por fim, verifica os resultados do teste usando as funções assert do JUnit, como assertEquals.
 *
 * @author Wilson Ventura
 * @since 2023
 */

class RestApiJUnitTests {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserController userController;

	private User user;

	@BeforeEach
	void setup() {

		// inicializa os campos anotados com @Mock e os injeta com @InjectMocks no controlador
		MockitoAnnotations.openMocks(this);

		// Utilização do design patter Builder por meio do Lombok
		user = User.builder()
				.id(1L)
				.name("Luiz Inacio")
				.surName("da Silva")
				.birthDate(LocalDate.of(1972, Month.FEBRUARY, 22))
				.cpf("123.456.789-10")
				.email("lula@prov.com")
				.build();

		Address addressTest = new Address("Avenida", "Glasshouse, 69", "1001", "Rio 40º", "RJ", "69.069-069");
		addressTest.setId(1L);
		user.setAddress(addressTest);

		PhoneNumber phone = new PhoneNumber("Cel", "(21) 96687-8776");
		phone.setUser(user);
		phone.setId(1L);
		Set<PhoneNumber> phoneTest = new HashSet<>();
		phoneTest.add(phone);
		user.setPhones(phoneTest);
	}

	@Test
	void testCreateUser() {

		when(userRepository.save(user)).thenReturn(user);

		ResponseEntity<User> response = userController.createUser(user);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(user, response.getBody());
	}

	@Test
	void testGetUserById() {

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		ResponseEntity<User> response = userController.getUserById(1L);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(user, response.getBody());
	}

	@Test
	void testGetUsers() {

		List<User> userList = new ArrayList<>();
		userList.add(user);

		when(userRepository.findAll()).thenReturn(userList);

		ResponseEntity<List<User>> response = userController.getUsers();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(userList, response.getBody());
	}

	@Test
	void testUpdateUser() {

		User updatedUser = User.builder()
				.id(1L)
				.name("Dilma")
				.surName("Rousseff")
				.birthDate(LocalDate.of(1956, Month.OCTOBER, 26))
				.cpf("789.456.123-10")
				.email("dilmae@prov.com")
				.build();

		Address address = new Address("Avenida", "Planalto, 69", "1001", "Rio", "RJ", "69.069-069");
		address.setId(1L);
		updatedUser.setAddress(address);

		PhoneNumber phone = new PhoneNumber("Cel", "(21) 98966-2377");
		phone.setUser(updatedUser);
		phone.setId(1L);
		Set<PhoneNumber> phoneSet = new HashSet<>();
		phoneSet.add(phone);
		updatedUser.setPhones(phoneSet);

		when(userRepository.save(updatedUser)).thenReturn(updatedUser);
		when(userRepository.findById(updatedUser.getId())).thenReturn(Optional.of(updatedUser));


		ResponseEntity<User> response = userController.updateUser(user.getId(), updatedUser);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updatedUser.getName(), response.getBody().getName());
		assertEquals(updatedUser.getEmail(), response.getBody().getEmail());

	}

	@Test
	void testDeleteUser() {

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		ResponseEntity<User> response = userController.deleteUser(1L);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(userRepository, times(1)).delete(user);
	}
}
