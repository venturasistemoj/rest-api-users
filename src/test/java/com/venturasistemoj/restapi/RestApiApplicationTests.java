package com.venturasistemoj.restapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.venturasistemoj.restapi.domain.Address;
import com.venturasistemoj.restapi.domain.PhoneNumber;
import com.venturasistemoj.restapi.domain.User;
import com.venturasistemoj.restapi.domain.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RestApiApplicationTests {

	private static final String URL_API = "/rest-api/users";

	@Autowired private TestRestTemplate restTemplate;
	@Autowired private UserRepository userRepository;


	@Test
	public void testCreatedUser() {

		User userTest = new User("Wilson", "Ventura", LocalDate.of(1972, Month.FEBRUARY, 22), "123.456.789-10", "mycock@prov.com");

		Address addressTest = new Address("Ave", "Dickhouse, 69", "1001", "Rio 40º", "RJ", "69.069-069");
		addressTest.setUser(userTest);
		userTest.setAddress(addressTest);

		PhoneNumber phone = new PhoneNumber("Cel", "(21) 96687-8776");
		phone.setUser(userTest);
		Set<PhoneNumber> phoneTest = new HashSet<>();
		phoneTest.add(phone);
		userTest.setPhones(phoneTest);

		ResponseEntity<User> responseEntity = restTemplate.postForEntity(URL_API, userTest, User.class);
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		Optional<User> savedUser = userRepository.findById(responseEntity.getBody().getUserId());
		assertTrue(savedUser.isPresent());
	}

}
