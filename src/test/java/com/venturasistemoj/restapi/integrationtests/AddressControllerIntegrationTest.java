package com.venturasistemoj.restapi.integrationtests;

import com.venturasistemoj.restapi.domain.address.AddressDTO;
import com.venturasistemoj.restapi.domain.address.AddressService;
import com.venturasistemoj.restapi.domain.phone.PhoneService;
import com.venturasistemoj.restapi.domain.user.UserDTO;
import com.venturasistemoj.restapi.domain.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AddressControllerIntegrationTest {

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

    private UserDTO sampleUser;

    @BeforeEach
    public void setUp() {
        sampleUser = UserDTO.createSampleUser();

        userService.createUser(sampleUser);
    }

    @AfterEach
    public void cleanUp() throws ChangeSetPersister.NotFoundException {
        UserDTO userDTO = userService.getUserById(sampleUser.userId());
        if (userDTO != null)
            userService.deleteUser(userDTO.userId());
    }

    private final String URL_BASE = "/rest-api/adresses/";

    @Test
    public void testCreateAddress() {

        AddressDTO addressDTO = new AddressDTO(
                1L,
                "Avenida",
                "Teste",
                "5555",
                "6666",
                "RJ",
                "7777777",
                sampleUser
        );

        ResponseEntity<?> response = restTemplate.postForEntity(URL_BASE.concat("{userId}"), addressDTO, Object.class, sampleUser.userId());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ResponseEntity<AddressDTO> responseAddresses = restTemplate.getForEntity(URL_BASE.concat("{userId}"), AddressDTO.class, sampleUser.userId());

        assertEquals(HttpStatus.OK, responseAddresses.getStatusCode());
        assertTrue(responseAddresses.getBody() != null);
    }


}

