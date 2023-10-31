package com.venturasistemoj.restapi.integrationtests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import org.springframework.transaction.annotation.Transactional;

import com.venturasistemoj.restapi.domain.address.AddressDTO;
import com.venturasistemoj.restapi.domain.address.AddressService;
import com.venturasistemoj.restapi.domain.phone.PhoneNumberDTO;
import com.venturasistemoj.restapi.domain.phone.PhoneService;
import com.venturasistemoj.restapi.domain.user.UserDTO;
import com.venturasistemoj.restapi.domain.user.UserService;
import com.venturasistemoj.restapi.exceptions.IllegalAddressStateException;
import com.venturasistemoj.restapi.exceptions.IllegalOperationException;
import com.venturasistemoj.restapi.exceptions.IllegalPhoneStateException;
import com.venturasistemoj.restapi.exceptions.IllegalUserStateException;

/**
 * <h2>Class of integration tests for managing <code>user</code>, <code>addresses</code> and <code>phones</code> RESTful APIs.</h2>
 *
 * <p>The <code>@SpringBootTest</code> annotation loads the entire context of the application, including components,
 * services, controllers and the database.
 * <p>In addition to the regular Spring <code>TestContextFramework</code>, it provides support for different web
 * environment modes, including the ability to start a running web server listening on a defined or random port, among
 * others.
 *
 * <p>It can register the following beans for web tests that are using a running web server:
 * <ul>
 * <li><code>TestRestTemplate</code>
 * <li><code>WebTestClient</code>
 * <li><code>HttpGraphQlTester</code>
 * </ul>
 *
 * <p><code>TestRestTemplate</code> provides a more realistic way to test HTTP requests. It starts an embedded server
 * and makes real HTTP calls to the running API. Allows us to test the integration between various system components,
 * including controllers, service layer and persistence layer. Using <code>TestRestTemplate</code> is more suitable
 * for integration tests that span multiple layers of the system.</p>
 *
 * <p>The <code>@Transactional</code> annotation describes a transaction attribute on an individual method or on a class.
 * When this annotation is declared at the class level, it applies as a default to all methods of the declaring class
 * and its subclasses.
 * <p>If no custom rollback rules are configured in this annotation, the transaction will roll back on
 *
 * <p>JUnit 5, by default, runs test methods in random order to avoid dependencies and ensure that tests are isolated.
 * This is done to prevent the execution order of tests from affecting their results. <code>@TestMethodOrder</code> is
 * a type annotation used to configure a MethodOrderer for methods annotated with <code>@Test</code>.</p>
 * <p>If <code>@TestMethodOrder</code> is not explicitly declared on a test class, inherited from a parent class, or
 * declared on a test interface implemented by a test class, test methods will be ordered using a default algorithm
 * that is deterministic but intentionally nonobvious.
 * <p>JUnit 5 <code>@Order</code> annotation is used to configure the order in which the annotated element (i.e.,
 * field, method, or class) should be evaluated or executed relative to other elements of the same category.
 * When used with <code>MethodOrderer.OrderAnnotation</code>, the category applies to <code>@Test</code> methods.
 * <p>Since this system has a business rule that imposes the need to register a user before creating an address or
 * telephone number, the <code>createUserTest</code> method must be executed before all others and the
 * <code>deleteUserTest</code> method must be executed as a final test method. So they are annotated with
 * <code>@Order</code>.
 *
 * @author Wilson Ventura
 */

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpringRestApiTests {

	// Base URLs of system APIs.
	private final String USERS_API_URL = "/rest-api/users";
	private final String ADRESSES_API_URL = "/rest-api/adresses";
	private final String PHONES_API_URL = "/rest-api/phones";

	// Starts an embedded server and makes real HTTP calls to the running API.
	@Autowired private TestRestTemplate restTemplate;

	// API services.
	@Autowired private UserService userService;
	@Autowired private AddressService addressService;
	@Autowired private PhoneService phoneService;

	private UserDTO userTest;
	private AddressDTO addressTest;
	private PhoneNumberDTO phoneTest;
	private static final Long TEST_ID = 1L;

	/**
	 * <bold>Initial configuration performed before EACH <code>@Test</code> method.</bold>
	 */
	@BeforeEach
	public void beforeTestMethods() {

		userTest = UserDTO.builder()
				.userId(TEST_ID)
				.name("Luiz Inacio")
				.surName("da Silva")
				.birthDate(LocalDate.of(1972, Month.FEBRUARY, 22))
				.cpf("123.456.789-10")
				.email("lula@prov.com")
				.build();

		addressTest = AddressDTO.builder()
				.addressId(TEST_ID)
				.publicPlace("Avenida")
				.streetAddress("Glasshouse, 69")
				.complement("1001")
				.city("Rio 40º")
				.state("RJ")
				.zipCode("69.069-069")
				.userDTO(userTest)
				.build();

		phoneTest = PhoneNumberDTO.builder()
				.phoneId(TEST_ID)
				.type("Cel")
				.number("(21) 96687-8776")
				.userDTO(userTest)
				.build();
	}

	/**
	 * <bold>Final configuration performed after EACH <code>@Test</code> method.</bold>
	 * @throws <code>NotFoundException</code>
	 */
	@AfterEach
	public void afterTestMethods() {
		// ...

	}

	/**
	 * Sends a POST request to the user's API URL with the user in the request body.
	 * Checks response status and returned user data with helper method.
	 * @throws NotFoundException
	 * @throws <code>IllegalUserStateException</code>
	 * @throws <code>IllegalArgumentException</code>
	 */
	@Test
	@Order(1)
	public void createUserTest() throws IllegalUserStateException, IllegalArgumentException, NotFoundException {

		ResponseEntity<UserDTO> response = restTemplate.postForEntity(USERS_API_URL, userTest, UserDTO.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody().getUserId());
		assertUser(response);
	}

	/**
	 * <p>Sends a POST request to the adresses's API URL with the user ID in the path variable and the address in the request body.
	 * <p>Checks the response status and returned address data with helper method.
	 * @throws <code>NotFoundException</code>
	 * @throws <code>IllegalAddressStateException</code>
	 * @throws <code>IllegalOperationException</code>
	 */
	@Test
	@Order(2)
	public void createAddressTest() throws NotFoundException, IllegalAddressStateException, IllegalOperationException {

		ResponseEntity<AddressDTO> response = restTemplate.postForEntity(ADRESSES_API_URL + TEST_ID, addressTest, AddressDTO.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody().getAddressId());
		assertAddress(response);
	}

	/**
	 * <p>Sends a POST request to the phone's API URL with the user ID in the path variable and the phone number in the request body.
	 * <p>Checks the response status and returned phone number data with helper method.
	 * @throws <code>NotFoundException</code>
	 * @throws <code>IllegalPhoneStateException</code>
	 */
	@Test
	@Order(3)
	public void createPhoneTest() throws NotFoundException, IllegalPhoneStateException {

		System.err.println(userTest.getEmail());

		ResponseEntity<PhoneNumberDTO> response = restTemplate.postForEntity(PHONES_API_URL + TEST_ID, phoneTest, PhoneNumberDTO.class);

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

		ResponseEntity<UserDTO> response = restTemplate.getForEntity(USERS_API_URL + TEST_ID, UserDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertUser(response);
	}

	/**
	 * Sends a GET request for the user address with the user ID in the path variable.
	 * Checks the response status and returned user data with helper method.
	 */
	@Test
	public void getUserAddressTest() {

		ResponseEntity<AddressDTO> response = restTemplate.getForEntity(ADRESSES_API_URL + TEST_ID, AddressDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertAddress(response);
	}

	/**
	 * Sends a GET request for the user phone numbers with the user ID in the path variable.
	 * Checks the response status and returned user data with helper method.
	 */
	@Test
	public void getUserPhonesTest() {

		ResponseEntity<Set<PhoneNumberDTO>> response = restTemplate.exchange(PHONES_API_URL + TEST_ID, HttpMethod.GET, null,
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
	public void getDatabaseUsersTest() {

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
	 */
	@Test
	public void getDatabaseAdressesTest() {

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
	 */
	@Test
	public void getDatabasePhonesTest() {

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

		ResponseEntity<UserDTO> response = restTemplate.exchange(USERS_API_URL + TEST_ID, HttpMethod.PUT, requestUpdate, UserDTO.class);

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
	 */
	@Test
	public void updateUserAddressTest() {

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
				ADRESSES_API_URL + TEST_ID, HttpMethod.PUT, requestUpdate, AddressDTO.class);

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
	 */
	@Test
	public void updateUserPhoneTest() {

		PhoneNumberDTO updatedPhone = PhoneNumberDTO.builder()
				.type("Moblie")
				.number("(21) 96387-1788")
				.build();

		HttpEntity<PhoneNumberDTO> requestUpdate = new HttpEntity<>(updatedPhone);

		ResponseEntity<PhoneNumberDTO> response = restTemplate.exchange(
				PHONES_API_URL + TEST_ID, HttpMethod.PUT, requestUpdate, PhoneNumberDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(updatedPhone.getType(), response.getBody().getType());
		assertEquals(updatedPhone.getNumber(), response.getBody().getNumber());
	}

	/**
	 * Sends a DELETE request for the adresses API URL with the user ID and checks response status.
	 */
	@Test
	public void deleteUserAddressTest() {

		ResponseEntity<Void> response = restTemplate.exchange(ADRESSES_API_URL + TEST_ID, HttpMethod.DELETE, null, Void.class);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	/**
	 * Sends a DELETE request for the phones API URL with the user ID and checks response status.
	 */
	@Test
	public void deleteUserPhoneTest() {

		ResponseEntity<Void> response = restTemplate.exchange(PHONES_API_URL + TEST_ID, HttpMethod.DELETE, null, Void.class);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	/**
	 * Sends a DELETE request for the users API URL with the user ID and checks response status.
	 * <p>The <code>Void</code> class is a non-instantiable placeholder class to contain a reference to the
	 * <code>Class<code> object representing the Java keyword <code>void</code>.
	 */
	@Test
	@Order(15)
	public void deleteUserTest() {

		ResponseEntity<Void> response = restTemplate.exchange(USERS_API_URL + TEST_ID, HttpMethod.DELETE, null, Void.class);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	// helper method for user assertions
	private void assertUser(ResponseEntity<UserDTO> userResponse) {
		assertEquals(userTest.getName(), userResponse.getBody().getName());
		assertEquals(userTest.getSurName(), userResponse.getBody().getSurName());
		assertEquals(userTest.getBirthDate(), userResponse.getBody().getBirthDate());
		assertEquals(userTest.getCpf(), userResponse.getBody().getCpf());
		assertEquals(userTest.getEmail(), userResponse.getBody().getEmail());
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
