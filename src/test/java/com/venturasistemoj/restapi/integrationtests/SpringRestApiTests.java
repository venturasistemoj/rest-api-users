package com.venturasistemoj.restapi.integrationtests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
//@Transactional
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

		userTest = UserDTO.builder()
				.userId(1L)
				.name("Luiz Inacio")
				.surName("da Silva")
				.birthDate(LocalDate.of(1972, Month.FEBRUARY, 22))
				.cpf("123.456.789-10")
				.email("lula@prov.com")
				.build();

		addressTest = AddressDTO.builder()
				.addressId(1L)
				.publicPlace("Avenida")
				.streetAddress("Glasshouse, 69")
				.complement("1001")
				.city("Rio 40º")
				.state("RJ")
				.zipCode("69.069-069")
				.build();

		//addressTest.setUserDTO(userTest);
		//userTest.setAddressDTO(addressTest);

		phoneTest = PhoneNumberDTO.builder()
				.phoneId(1L)
				.type("Cel")
				.number("(21) 96687-8776")
				.build();

		//phoneTest.setUserDTO(userTest);
		//Set<PhoneNumberDTO> phones = new HashSet<>();
		//phones.add(phoneTest);
		//userTest.setPhonesDTO(phones);

		userService.createUser(userTest);
		//addressService.createAddress(userTest.getUserId(), addressTest);
		//phoneService.createPhoneNumber(userTest.getUserId(), phoneTest);
	}

	@AfterEach
	public void afterSetUp() throws NotFoundException {
		//userService.deleteUser(userTest.getUserId());
		//addressService.deleteAddress(userTest.getUserId());
		//phoneService.deletePhoneNumber(userTest.getUserId(), phoneTest);
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

	/**
	 * Sends a POST request to the user's API URL with the user in the request body.
	 * Checks response status and returned user data with helper method.
	 * @throws NotFoundException
	 */
	@Test
	public void createUserTest() throws NotFoundException {

		userService.deleteUser(userTest.getUserId());

		ResponseEntity<UserDTO> response = restTemplate.postForEntity(
				USERS_API_URL, userTest, UserDTO.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody().getUserId());
		assertUser(response);
	}

	/**
	 * Sends a POST request to the address's API URL with the user ID in the path variable.
	 * Checks the response status and returned address data with helper method.
	 * @throws NotFoundException
	 */
	@Test
	public void createAddressTest() throws NotFoundException {

		//addressService.deleteAddress(userTest.getUserId());

		ResponseEntity<AddressDTO> response = restTemplate.postForEntity(
				ADRESSES_API_URL + userTest.getUserId(), addressTest, AddressDTO.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody().getAddressId());
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

		//phoneService.deletePhoneNumber(userTest.getUserId(), phoneTest);

		ResponseEntity<PhoneNumberDTO> response = restTemplate.postForEntity(
				PHONES_API_URL + userTest.getUserId(), phoneTest, PhoneNumberDTO.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody().getPhoneId());
		assertPhone(response);
	}

	/**
	 * Sends a GET request for a user.
	 * Checks the response status and returned user data with helper method.
	 */
	@Test
	public void getUserTest() {

		ResponseEntity<UserDTO> response = restTemplate.getForEntity(
				USERS_API_URL + userTest.getUserId(), UserDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertUser(response);
	}

	/**
	 * Sends a GET request for the user address .
	 * Checks the response status and returned user data with helper method.
	 * @throws NotFoundException
	 * @throws IllegalStateException
	 * @throws IllegalAddressStateException
	 */
	@Test
	public void getUserAddressTest() throws IllegalAddressStateException, IllegalStateException, NotFoundException {

		addressService.createAddress(userTest.getUserId(), addressTest);

		ResponseEntity<AddressDTO> response = restTemplate.getForEntity(
				ADRESSES_API_URL + userTest.getUserId(), AddressDTO.class);

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

		phoneService.createPhoneNumber(userTest.getUserId(), phoneTest);

		ResponseEntity<Set<PhoneNumberDTO>> response = restTemplate.exchange(
				PHONES_API_URL + userTest.getUserId(), HttpMethod.GET, null,
				new ParameterizedTypeReference<Set<PhoneNumberDTO>>() {
				});

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(phoneTest, response.getBody().iterator().next());
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
		assertEquals(userTest.getUserId(), response.getBody().get(0).getUserId());
		assertEquals(userTest.getName(), response.getBody().get(0).getName());
		assertEquals(userTest.getEmail(), response.getBody().get(0).getEmail());
		assertEquals(userTest.getCpf(), response.getBody().get(0).getCpf());
		assertEquals(userTest.getBirthDate(), response.getBody().get(0).getBirthDate());
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

		addressService.createAddress(userTest.getUserId(), addressTest);

		ResponseEntity<List<AddressDTO>> response = restTemplate.exchange(ADRESSES_API_URL, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<AddressDTO>>() {
		});

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(addressTest.getAddressId(), response.getBody().get(0).getAddressId());
		assertEquals(addressTest.getPublicPlace(), response.getBody().get(0).getPublicPlace());
		assertEquals(addressTest.getStreetAddress(), response.getBody().get(0).getStreetAddress());
		assertEquals(addressTest.getComplement(), response.getBody().get(0).getComplement());
		assertEquals(addressTest.getCity(), response.getBody().get(0).getCity());
		assertEquals(addressTest.getState(), response.getBody().get(0).getState());
		assertEquals(addressTest.getZipCode(), response.getBody().get(0).getZipCode());
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

		phoneService.createPhoneNumber(userTest.getUserId(), phoneTest);

		ResponseEntity<Set<PhoneNumberDTO>> response = restTemplate.exchange(PHONES_API_URL, HttpMethod.GET, null,
				new ParameterizedTypeReference<Set<PhoneNumberDTO>>() {
		});

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(phoneTest.getType(), response.getBody().iterator().next().getType());
		assertEquals(phoneTest.getNumber(), response.getBody().iterator().next().getNumber());
	}

	/**
	 * Creates a new user with updated data.
	 * Creates an HTTP entity with the updated user.
	 * Sends a PUT request to the user URL with the user ID and the updated HTTP entity.
	 */
	@Test
	public void updateUserTest() {

		UserDTO updatedUser = UserDTO.builder()
				.name("Dilma")
				.surName("Rousseff")
				.birthDate(LocalDate.of(1956, Month.OCTOBER, 26))
				.cpf("789.456.123-10")
				.email("dilmae@prov.com")
				.build();

		HttpEntity<UserDTO> requestUpdate = new HttpEntity<>(updatedUser);

		ResponseEntity<UserDTO> response = restTemplate.exchange(
				USERS_API_URL + userTest.getUserId(), HttpMethod.PUT, requestUpdate, UserDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(updatedUser.getUserId(), response.getBody().getUserId());
		assertEquals(updatedUser.getName(), response.getBody().getName());
		assertEquals(updatedUser.getEmail(), response.getBody().getEmail());
		assertEquals(updatedUser.getCpf(), response.getBody().getCpf());
		assertEquals(updatedUser.getBirthDate(), response.getBody().getBirthDate());
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

		addressService.createAddress(userTest.getUserId(), addressTest);

		AddressDTO updatedAddress = AddressDTO.builder()
				.publicPlace("Rua")
				.streetAddress("Conde Deu, 69")
				.complement("801")
				.city("Salvador")
				.state("BA")
				.zipCode("69.069-069")
				.build();

		HttpEntity<AddressDTO> requestUpdate = new HttpEntity<>(updatedAddress);

		ResponseEntity<AddressDTO> response = restTemplate.exchange(
				ADRESSES_API_URL + userTest.getUserId(), HttpMethod.PUT, requestUpdate, AddressDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(updatedAddress.getAddressId(), response.getBody().getAddressId());
		assertEquals(updatedAddress.getPublicPlace(), response.getBody().getPublicPlace());
		assertEquals(updatedAddress.getStreetAddress(), response.getBody().getStreetAddress());
		assertEquals(updatedAddress.getComplement(), response.getBody().getComplement());
		assertEquals(updatedAddress.getCity(), response.getBody().getCity());
		assertEquals(updatedAddress.getState(), response.getBody().getState());
		assertEquals(updatedAddress.getZipCode(), response.getBody().getZipCode());
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

		phoneService.createPhoneNumber(userTest.getUserId(), phoneTest);

		PhoneNumberDTO updatedPhone = PhoneNumberDTO.builder()
				.type("Moblie")
				.number("(21) 96387-1788")
				.build();

		HttpEntity<PhoneNumberDTO> requestUpdate = new HttpEntity<>(updatedPhone);

		ResponseEntity<PhoneNumberDTO> response = restTemplate.exchange(
				PHONES_API_URL + userTest.getUserId(), HttpMethod.PUT, requestUpdate, PhoneNumberDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(updatedPhone.getType(), response.getBody().getType());
		assertEquals(updatedPhone.getNumber(), response.getBody().getNumber());
	}

	/**
	 * Sends a DELETE request for the users API URL with the user ID and checks response status.
	 * The Void class is a non-instantiable placeholder class to contain a reference to the Class object
	 * representing the Java keyword void.
	 */
	@Test
	public void deleteUserTest() {

		ResponseEntity<Void> response = restTemplate.exchange(
				USERS_API_URL + userTest.getUserId(), HttpMethod.DELETE, null, Void.class);

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

		addressService.createAddress(userTest.getUserId(), addressTest);

		ResponseEntity<Void> response = restTemplate.exchange(
				ADRESSES_API_URL + userTest.getUserId(), HttpMethod.DELETE, null, Void.class);

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

		phoneService.createPhoneNumber(userTest.getUserId(), phoneTest);

		ResponseEntity<Void> response = restTemplate.exchange(
				PHONES_API_URL + userTest.getUserId(), HttpMethod.DELETE, null, Void.class);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	// helper method for user assertions
	private void assertUser(ResponseEntity<UserDTO> userResponse) {
		assertEquals(userTest.getName(), userResponse.getBody().getName());
		assertEquals(userTest.getEmail(), userResponse.getBody().getEmail());
		assertEquals(userTest.getCpf(), userResponse.getBody().getCpf());
		assertEquals(userTest.getBirthDate(), userResponse.getBody().getBirthDate());
	}

	// helper method for address assertions
	private void assertAddress(ResponseEntity<AddressDTO> addressResponse) {
		assertEquals(addressTest.getPublicPlace(), addressResponse.getBody().getPublicPlace());
		assertEquals(addressTest.getStreetAddress(), addressResponse.getBody().getStreetAddress());
		assertEquals(addressTest.getComplement(), addressResponse.getBody().getComplement());
		assertEquals(addressTest.getCity(), addressResponse.getBody().getCity());
		assertEquals(addressTest.getState(), addressResponse.getBody().getState());
		assertEquals(addressTest.getZipCode(), addressResponse.getBody().getZipCode());
	}

	// helper method for phone assertions
	private void assertPhone(ResponseEntity<PhoneNumberDTO> phoneResponse) {
		assertEquals(phoneTest.getType(), phoneResponse.getBody().getType());
		assertEquals(phoneTest.getNumber(), phoneResponse.getBody().getNumber());
	}

}
