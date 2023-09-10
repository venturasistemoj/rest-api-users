package com.venturasistemoj.restapi.integrationtests;

import com.venturasistemoj.restapi.domain.address.AddressDTO;
import com.venturasistemoj.restapi.domain.address.AddressService;
import com.venturasistemoj.restapi.domain.phone.PhoneNumberDTO;
import com.venturasistemoj.restapi.domain.phone.PhoneService;
import com.venturasistemoj.restapi.domain.user.User;
import com.venturasistemoj.restapi.domain.user.UserDTO;
import com.venturasistemoj.restapi.domain.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;


    // User API service.
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private PhoneService phoneService;

    // You can add other dependencies and setup as needed

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void cleanUp() throws ChangeSetPersister.NotFoundException {
    }

    private final String URL_BASE = "/rest-api/users";



    @Test
    public void testCreateUser() {

        UserDTO userDTO = UserDTO.exampleComplete();

        ResponseEntity<?> response = restTemplate.postForEntity(URL_BASE, userDTO, Object.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ResponseEntity<UserDTO> responseAddresses = restTemplate.getForEntity(URL_BASE.concat("{userId}"), UserDTO.class, 1L);

        assertEquals(HttpStatus.OK, responseAddresses.getStatusCode());
        assertTrue(responseAddresses.getBody() != null);
    }


}

