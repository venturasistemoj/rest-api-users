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

import com.venturasistemoj.restapi.controllers.UserController;
import com.venturasistemoj.restapi.domain.user.UserDTO;
import com.venturasistemoj.restapi.domain.user.UserService;

/**
 * <p>JUnit test class for <code>UserController</code>, focusing on operations related to user management.
 * Use Mockito to 'mock' <code>UserService</code> and test the behavior of <code>UserController</code>.
 * Before each test case, the required mock objects are initialized and injected into the controller. Additionally,
 * it uses the Builder design pattern provided by Lombok to create <code>UserDTO</code> instances for testing.</p>
 *
 * <p>The test methods cover the following scenarios:</p>
 * <ul>
 * <li>1. Creates a user by simulating the <code>createUser</code> method of <code>UserService</code> and asserts the
 * HTTP response.
 * <li>2. Updates a user by simulating the <code>getUserById</code> and <code>updateUser</code> methods of
 * <code>UserService</code> and asserts the response.
 * <li>3. Retrieves a user by simulating <code>getUserById</code> from <code>UserService</code> and validates the returned
 * response.
 * <li>4. List users by simulating the <code>getUsers</code> method of <code>UserService</code> and compare the expected
 * list with the response.
 * <li>5. Deletes a user by simulating <code>getUserById</code> from <code>UserService</code> and verifies the deletion
 * via the HTTP response and the <code>deleteUser</code> method call.
 * </ul>
 *
 * See {@link com.venturasistemoj.restapi.unitests.JUnitAddressTests}
 * See {@link com.venturasistemoj.restapi.unitests.JUnitPhoneTests}
 *
 * @author Wilson Ventura
 */

class JUnitUserTests {

	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;

	private UserDTO userDTO;
	private final Long userId = 1L;

	@BeforeEach
	void setup() {

		// initializes fields annotated with @Mock and injects them with @InjectMocks into the controller
		MockitoAnnotations.openMocks(this);

		// Use of the design pattern Builder by Lombok
		userDTO = UserDTO.builder()
				.userId(userId)
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

		UserDTO updatedUser = UserDTO.builder()
				.userId(userId)
				.name("Dilma")
				.surName("Rousseff")
				.birthDate(LocalDate.of(1956, Month.OCTOBER, 26))
				.cpf("789.456.123-10")
				.email("dilma@prov.com")
				.build();

		when(userService.updateUser(userId, updatedUser)).thenReturn(updatedUser);

		ResponseEntity<?> response = userController.updateUser(userId, updatedUser);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updatedUser.getName(), ((UserDTO) response.getBody()).getName());
		assertEquals(updatedUser.getSurName(), ((UserDTO) response.getBody()).getSurName());
		assertEquals(updatedUser.getBirthDate(), ((UserDTO) response.getBody()).getBirthDate());
		assertEquals(updatedUser.getCpf(), ((UserDTO) response.getBody()).getCpf());
		assertEquals(updatedUser.getEmail(), ((UserDTO) response.getBody()).getEmail());
	}

	@Test
	void testGetUserById() throws NotFoundException {

		when(userService.getUserById(userId)).thenReturn(userDTO);

		ResponseEntity<?> response = userController.getUserById(userId);

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

		when(userService.getUserById(userId)).thenReturn(userDTO);

		ResponseEntity<?> response = userController.deleteUser(userId);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(userService, times(1)).deleteUser(userId);
	}
}
