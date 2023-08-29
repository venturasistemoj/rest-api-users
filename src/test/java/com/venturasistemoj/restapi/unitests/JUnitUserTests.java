package com.venturasistemoj.restapi.unitests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.venturasistemoj.restapi.domain.user.UserController;
import com.venturasistemoj.restapi.domain.user.UserDTO;
import com.venturasistemoj.restapi.domain.user.UserService;

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
 * Esta classe utiliza a biblioteca Mockito para criar mocks de UserService e os injeta no UserController com a
 * anotação @InjectMocks. Também utiliza a anotação @BeforeEach para inicializar os mocks antes de cada teste usando
 * MockitoAnnotations.openMocks(this).
 *
 * Em cada teste, configura o comportamento esperado dos mocks usando a função when e thenReturn do Mockito.
 * Por fim, verifica os resultados do teste usando as funções assert do JUnit, como assertEquals.
 *
 * @author Wilson Ventura
 * @since 2023
 */

class JUnitUserTests {

	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;

	private UserDTO userDTO;

	@BeforeEach
	void setup() {

		// inicializa os campos anotados com @Mock e os injeta com @InjectMocks no controlador
		MockitoAnnotations.openMocks(this);

		// Utilização do design patter Builder por meio do Lombok
		userDTO = UserDTO.builder()
				.userId(1L)
				.name("Luiz Inacio")
				.surName("da Silva")
				.birthDate(LocalDate.of(1972, Month.FEBRUARY, 22))
				.cpf("123.456.789-10")
				.email("lula@prov.com")
				.build();
	}

	@Test
	void testCreateUser() {

		when(userService.createUser(userDTO)).thenReturn(userDTO);

		ResponseEntity<?> response = userController.createUser(userDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(userDTO, response.getBody());
	}

	@Test
	void testUpdateUser() throws NotFoundException {

		UserDTO newUser = UserDTO.builder()
				.userId(1L)
				.name("Dilma")
				.surName("Rousseff")
				.birthDate(LocalDate.of(1956, Month.OCTOBER, 26))
				.cpf("789.456.123-10")
				.email("dilma@prov.com")
				.build();

		final Long userId = newUser.getUserId();
		when(userService.updateUser(userId, newUser)).thenReturn(newUser);
		when(userService.getUserById(userId)).thenReturn(newUser);

		ResponseEntity<?> response = userController.updateUser(userId, newUser);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(newUser.getSurName(), ((UserDTO) response.getBody()).getSurName());
		assertEquals(newUser.getEmail(), ((UserDTO) response.getBody()).getEmail());
		assertEquals(newUser.getCpf(), ((UserDTO) response.getBody()).getCpf());
	}

	@Test
	void testGetUserById() throws NotFoundException {

		when(userService.getUserById(1L)).thenReturn(userDTO);

		ResponseEntity<?> response = userController.getUserById(1L);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(userDTO, response.getBody());
	}

	@Test
	void testGetUsers() throws NotFoundException {

		List<UserDTO> userList = new ArrayList<>();
		userList.add(userDTO);
		when(userService.getUsers()).thenReturn(userList);

		ResponseEntity<?> response = userController.getUsers();

		@SuppressWarnings("unchecked")
		List<UserDTO> expectedList = (List<UserDTO>) response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(userList.containsAll(expectedList));
		assertEquals(userList, response.getBody());
	}

	@Test
	void testDeleteUser() throws NotFoundException {

		when(userService.getUserById(1L)).thenReturn(userDTO);

		ResponseEntity<?> response = userController.deleteUser(1L);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(userService, times(1)).deleteUser(1L);
	}
}
