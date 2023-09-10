package com.venturasistemoj.restapi.integrationtests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.venturasistemoj.restapi.domain.address.AddressDTO;
import com.venturasistemoj.restapi.domain.address.AddressService;
import com.venturasistemoj.restapi.domain.address.IllegalAddressStateException;
import com.venturasistemoj.restapi.domain.phone.IllegalPhoneStateException;
import com.venturasistemoj.restapi.domain.phone.PhoneNumberDTO;
import com.venturasistemoj.restapi.domain.phone.PhoneService;
import com.venturasistemoj.restapi.domain.user.UserDTO;
import com.venturasistemoj.restapi.domain.user.UserService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test class for management of a RESTful API.
 *
 * The <code>@SpringBootTest</code> annotation can be specified in a test class that runs Spring Boot based tests.
 * It loads the entire context of the application, including components, services, controllers and the database.
 * In addition to the regular Spring TestContextFramework, it provides support for different web environment modes,
 * including the ability to start a running web server listening on a defined or random port, among others.
 * It can register the following beans for web tests that are using a running web server:
 * • <code>TestRestTemplate</code>
 * • <code>WebTestClient</code>
 * • <code>HttpGraphQlTester</code>
 *
 * <code>TestRestTemplate</code> provides a more realistic way to test HTTP requests. It starts an embedded server and
 * makes real HTTP calls to the running API. Allows us to test the integration between various system components,
 * including controllers, service layer and persistence layer. Using <code>TestRestTemplate</code> is more suitable
 * for integration tests that span multiple layers of the system.
 *
 * JUnit 5, by default, runs test methods in random order to avoid dependencies and ensure that tests are isolated.
 * This is done to prevent the execution order of tests from affecting their results. <code>@TestMethodOrder</code> is
 * a type annotation used to configure a MethodOrderer for methods annotated with <code>@Test</code>.
 *
 * The <code>@Transactional</code> annotation ensures that each test runs in a separate transaction and that
 * changes are rolled back after the test completes.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SpringRestApiTests {

	// System APIs base URLs.
	private final String USERS_API_URL = "/rest-api/users";
	private final String ADRESSES_API_URL = "/rest-api/adresses";
	private final String PHONES_API_URL = "/rest-api/phones";

	// Starts an embedded server and makes real HTTP calls to the running API.
	@Autowired private TestRestTemplate restTemplate;

	// User API service.
	@Autowired private UserService userService;
	@Autowired private AddressService addressService;
	@Autowired private PhoneService phoneService;

	private UserDTO userTest;
	private AddressDTO addressTest;
	private PhoneNumberDTO phoneTest;

	/**
	 * Initial teste setup. <code>@BeforeEach</code> is executed before EACH method annotated with <code>@Test</code>.
	 * This prevents one test from affecting the state of another.
	 * @throws NotFoundException
	 * @throws IllegalStateException
	 * @throws IllegalAddressStateException
	 */
	@BeforeEach
	public void beforeSetUp() throws IllegalAddressStateException, IllegalStateException, NotFoundException {

		addressTest = AddressDTO.createSampleAddress();

		phoneTest = PhoneNumberDTO.createSamplePhoneNumber();

		userTest = UserDTO.createSampleMaleUser(addressTest, phoneTest);

		userService.createUser(userTest);
	}

	@AfterEach
	public void afterSetUp() throws NotFoundException {
		userService.deleteUser(userTest.userId());
	}

	/**
	 * The <code>@DirtiesContext</code> annotation indicates that the <code>ApplicationContext</code> associated with
	 * the test is dirty and will be closed and removed from the context cache.
	 * This annotation is used if a test has modified the context — e.g. modifying the state of a singleton bean,
	 * an embedded database, etc. Subsequent tests that request the same context will be given a new context.
	 * <code>@DirtiesContext</code> can be used at class level and at method level within the same class or class
	 * hierarchy. In these scenarios, the <code>ApplicationContext</code> will be marked dirty before or after any
	 * annotated methods, as well as before or after the current test class, depending on the configured
	 * <code>methodMode</code> and <code>classMode</code>.
	 *
	 * @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	 * @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	 */

	public void testCreateNewAddress() {

	}

	/**
	 * Sends a POST request to the user's API URL with the user in the request body.
	 * Checks response status and returned user data with helper method.
	 * @throws NotFoundException
	 */
	@Test
	public void testUserWasCreatedSuccessfully() throws NotFoundException {

		userService.deleteUser(userTest.userId());

		ResponseEntity<UserDTO> response = restTemplate.postForEntity(
				USERS_API_URL, userTest, UserDTO.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody().userId());
		assertUser(response);
	}

	/**
	 * Sends a POST request to the address's API URL with the user ID in the path variable.
	 * Checks the response status and returned address data with helper method.
	 * @throws NotFoundException
	 */
	@Test
	public void createAddressTest() throws NotFoundException {

		//addressService.deleteAddress(userTest.userId());

		ResponseEntity<AddressDTO> response = restTemplate.postForEntity(
				ADRESSES_API_URL + userTest.userId(), addressTest, AddressDTO.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody().addressId());
		assertAddress(response);
	}

	/**
	 * Sends a POST request to the phone's API URL with the user ID in the path variable.
	 * Checks the response status and returned phone number data with helper method.
	 * @throws NotFoundException
	 * @throws IllegalPhoneStateException
	 */
	@Test
	public void createPhoneTest() throws IllegalPhoneStateException, NotFoundException {

		//phoneService.deletePhoneNumber(userTest.userId(), phoneTest);

		ResponseEntity<PhoneNumberDTO> response = restTemplate.postForEntity(
				PHONES_API_URL + userTest.userId(), phoneTest, PhoneNumberDTO.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody().phoneId());
		assertPhone(response);
	}

	/**
	 * Sends a GET request for a user.
	 * Checks the response status and returned user data with helper method.
	 */
	@Test
	public void getUserTest() {

		ResponseEntity<UserDTO> response = restTemplate.getForEntity(
				USERS_API_URL + userTest.userId(), UserDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertUser(response);
	}

	/**
	 * Sends a GET request for the user address with the user ID in the path variable.
	 * Checks the response status and returned user data with helper method.
	 * @throws NotFoundException
	 * @throws IllegalStateException
	 * @throws IllegalAddressStateException
	 */
	@Test
	public void getUserAddressTest() throws IllegalAddressStateException, IllegalStateException, NotFoundException {

		addressService.createAddress(userTest.userId(), addressTest);

		ResponseEntity<AddressDTO> response = restTemplate.getForEntity(
				ADRESSES_API_URL + userTest.userId(), AddressDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertAddress(response);
	}

	/**
	 * Sends a GET request for the user phone numbers with the user ID in the path variable.
	 * Checks the response status and returned user data with helper method.
	 * @throws NotFoundException
	 * @throws IllegalPhoneStateException
	 * @throws IllegalStateException
	 */
	@Test
	public void getUserPhonesTest() throws IllegalStateException, IllegalPhoneStateException, NotFoundException {

		phoneService.createPhoneNumber(userTest.userId(), phoneTest);

		ResponseEntity<String> response = restTemplate.getForEntity(PHONES_API_URL.concat("/").concat(userTest.userId().toString()), String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		//assertEquals(userTest, response.getBody().iterator().next().userDTO());
	}

	/**
	 * Sends a GET request for all database users.
	 * Checks the response status and returned user data for the only user previously stored.
	 */
	@Test
	public void getUsersTest() {

		ResponseEntity<List<UserDTO>> response = restTemplate.exchange(USERS_API_URL, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<UserDTO>>() {
		});

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(userTest.userId(), response.getBody().get(0).userId());
		assertEquals(userTest.name(), response.getBody().get(0).name());
		assertEquals(userTest.email(), response.getBody().get(0).email());
		assertEquals(userTest.cpf(), response.getBody().get(0).cpf());
		assertEquals(userTest.birthDate(), response.getBody().get(0).birthDate());
	}

	/**
	 * Sends a GET request for all database adresses.
	 * Checks the response status and returned user data for the only address previously stored.
	 * @throws NotFoundException
	 * @throws IllegalStateException
	 * @throws IllegalAddressStateException
	 */
	@Test
	public void getAdressesTest() throws IllegalAddressStateException, IllegalStateException, NotFoundException {

		addressService.createAddress(userTest.userId(), addressTest);

		ResponseEntity<List<AddressDTO>> response = restTemplate.exchange(ADRESSES_API_URL, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<AddressDTO>>() {
		});

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(addressTest.addressId(), response.getBody().get(0).addressId());
		assertEquals(addressTest.publicPlace(), response.getBody().get(0).publicPlace());
		assertEquals(addressTest.streetAddress(), response.getBody().get(0).streetAddress());
		assertEquals(addressTest.complement(), response.getBody().get(0).complement());
		assertEquals(addressTest.city(), response.getBody().get(0).city());
		assertEquals(addressTest.state(), response.getBody().get(0).state());
		assertEquals(addressTest.zipCode(), response.getBody().get(0).zipCode());
	}

	/**
	 * Sends a GET request for all database phone numbers.
	 * Checks the response status and returned user data for the only phone number previously stored.
	 * @throws NotFoundException
	 * @throws IllegalPhoneStateException
	 * @throws IllegalStateException
	 */
	@Test
	public void getPhonesTest() throws IllegalStateException, IllegalPhoneStateException, NotFoundException {

		phoneService.createPhoneNumber(userTest.userId(), phoneTest);

		ResponseEntity<Set<PhoneNumberDTO>> response = restTemplate.exchange(PHONES_API_URL, HttpMethod.GET, null, new ParameterizedTypeReference<Set<PhoneNumberDTO>>() {});

		assertEquals(HttpStatus.OK, response.getStatusCode());
		PhoneNumberDTO responseObj = response.getBody().stream().findFirst().orElse(null);
		assertNotNull(responseObj);

		assertEquals(phoneTest.type(), responseObj.type());
		assertEquals(phoneTest.number(), responseObj.number());
	}

	/**
	 * Creates a new user with updated data.
	 * Creates an HTTP entity with the updated user.
	 * Sends a PUT request to the user URL with the user ID and the updated HTTP entity.
	 */
	@Test
	public void updateUserTest() {

		UserDTO updatedUser = UserDTO.createSampleUser();

		HttpEntity<UserDTO> requestUpdate = new HttpEntity<>(updatedUser);

		ResponseEntity<UserDTO> response = restTemplate.exchange(
				USERS_API_URL + userTest.userId(), HttpMethod.PUT, requestUpdate, UserDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(updatedUser.userId(), response.getBody().userId());
		assertEquals(updatedUser.name(), response.getBody().name());
		assertEquals(updatedUser.email(), response.getBody().email());
		assertEquals(updatedUser.cpf(), response.getBody().cpf());
		assertEquals(updatedUser.birthDate(), response.getBody().birthDate());
	}

	/**
	 * Creates a new address with updated data.
	 * Creates an HTTP entity with the updated address.
	 * Sends a PUT request to the adrress URL with the user ID and the updated HTTP entity.
	 * @throws NotFoundException
	 * @throws IllegalStateException
	 * @throws IllegalAddressStateException
	 */
	@Test
	public void updateAddressTest() throws IllegalAddressStateException, IllegalStateException, NotFoundException {

		addressService.createAddress(userTest.userId(), addressTest);

		AddressDTO updatedAddress = new AddressDTO(null, "Rua", "Conde Dev, 69",
				"801", "Salvador", "BA", "69.069-069", null);

		HttpEntity<AddressDTO> requestUpdate = new HttpEntity<>(updatedAddress);

		ResponseEntity<AddressDTO> response = restTemplate.exchange(
				ADRESSES_API_URL + userTest.userId(), HttpMethod.PUT, requestUpdate, AddressDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(updatedAddress.addressId(), response.getBody().addressId());
		assertEquals(updatedAddress.publicPlace(), response.getBody().publicPlace());
		assertEquals(updatedAddress.streetAddress(), response.getBody().streetAddress());
		assertEquals(updatedAddress.complement(), response.getBody().complement());
		assertEquals(updatedAddress.city(), response.getBody().city());
		assertEquals(updatedAddress.state(), response.getBody().state());
		assertEquals(updatedAddress.zipCode(), response.getBody().zipCode());
	}

	/**
	 * Creates a new phone number with updated data.
	 * Creates an HTTP entity with the updated phone number.
	 * Sends a PUT request to the phone number URL with the user ID and the updated HTTP entity.
	 * @throws NotFoundException
	 * @throws IllegalPhoneStateException
	 * @throws IllegalStateException
	 */
	@Test
	public void updatePhoneTest() throws IllegalStateException, IllegalPhoneStateException, NotFoundException {

		phoneService.createPhoneNumber(userTest.userId(), phoneTest);

		PhoneNumberDTO updatedPhone = new PhoneNumberDTO(null, "Moblie", "(21) 96387-1788", null);

		HttpEntity<PhoneNumberDTO> requestUpdate = new HttpEntity<>(updatedPhone);

		ResponseEntity<PhoneNumberDTO> response = restTemplate.exchange(
				PHONES_API_URL + userTest.userId(), HttpMethod.PUT, requestUpdate, PhoneNumberDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(updatedPhone.type(), response.getBody().type());
		assertEquals(updatedPhone.number(), response.getBody().number());
	}

	/**
	 * Sends a DELETE request for the users API URL with the user ID and checks response status.
	 * The Void class is a non-instantiable placeholder class to contain a reference to the Class object
	 * representing the Java keyword void.
	 */
	@Test
	public void deleteUserTest() {

		ResponseEntity<Void> response = restTemplate.exchange(
				USERS_API_URL + userTest.userId(), HttpMethod.DELETE, null, Void.class);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	/**
	 * Sends a DELETE request for the adresses API URL with the user ID and checks response status.
	 * @throws NotFoundException
	 * @throws IllegalStateException
	 * @throws IllegalAddressStateException
	 */
	@Test
	public void deleteAddressTest() throws IllegalAddressStateException, IllegalStateException, NotFoundException {

		addressService.createAddress(userTest.userId(), addressTest);

		ResponseEntity<Void> response = restTemplate.exchange(
				ADRESSES_API_URL + userTest.userId(), HttpMethod.DELETE, null, Void.class);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	/**
	 * Sends a DELETE request for the phones API URL with the user ID and checks response status.
	 * @throws NotFoundException
	 * @throws IllegalPhoneStateException
	 * @throws IllegalStateException
	 */
	@Test
	public void deletePhoneTest() throws IllegalStateException, IllegalPhoneStateException, NotFoundException {

		phoneService.createPhoneNumber(userTest.userId(), phoneTest);

		ResponseEntity<Void> response = restTemplate.exchange(
				PHONES_API_URL + userTest.userId(), HttpMethod.DELETE, null, Void.class);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	// helper method for user assertions
	private void assertUser(ResponseEntity<UserDTO> userResponse) {
		assertEquals(userTest.name(), userResponse.getBody().name());
		assertEquals(userTest.email(), userResponse.getBody().email());
		assertEquals(userTest.cpf(), userResponse.getBody().cpf());
		assertEquals(userTest.birthDate(), userResponse.getBody().birthDate());
	}

	// helper method for address assertions
	private void assertAddress(ResponseEntity<AddressDTO> addressResponse) {
		assertEquals(addressTest.publicPlace(), addressResponse.getBody().publicPlace());
		assertEquals(addressTest.streetAddress(), addressResponse.getBody().streetAddress());
		assertEquals(addressTest.complement(), addressResponse.getBody().complement());
		assertEquals(addressTest.city(), addressResponse.getBody().city());
		assertEquals(addressTest.state(), addressResponse.getBody().state());
		assertEquals(addressTest.zipCode(), addressResponse.getBody().zipCode());
	}

	// helper method for phone assertions
	private void assertPhone(ResponseEntity<PhoneNumberDTO> phoneResponse) {
		assertEquals(phoneTest.type(), phoneResponse.getBody().type());
		assertEquals(phoneTest.number(), phoneResponse.getBody().number());
	}

}
