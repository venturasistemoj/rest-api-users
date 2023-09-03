package com.venturasistemoj.restapi.unitests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.venturasistemoj.restapi.domain.address.AddressDTO;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.venturasistemoj.restapi.domain.phone.PhoneController;
import com.venturasistemoj.restapi.domain.phone.PhoneNumberDTO;
import com.venturasistemoj.restapi.domain.phone.PhoneService;
import com.venturasistemoj.restapi.domain.user.UserDTO;

public class JUnitPhoneTests {

    @Mock
    PhoneService phoneService;

    @InjectMocks
    PhoneController phoneController;

    private UserDTO userDTO;
    private PhoneNumberDTO numberDTO;
    private final Long userId = 1L;

    @BeforeEach
    void setup() {

        // inicializa os campos anotados com @Mock e os injeta com @InjectMocks no controlador
        MockitoAnnotations.openMocks(this);

        Set<PhoneNumberDTO> newPhonesDTO = new HashSet<>();
        numberDTO = new PhoneNumberDTO(
                1L,
                "Cel",
                "(21) 96687-8776",
                userDTO // Associate the userDTO with the phone number during creation
        );
        newPhonesDTO.add(numberDTO);

        // Associa conjunto de telefones a usuário
        userDTO = new UserDTO(userId, "Luiz Inacio", "da Silva", LocalDate.of(1972, Month.FEBRUARY, 22), "123.456.789-10", "lula@prov.com", null, newPhonesDTO);
    }

    @Test
    void testCreatePhoneNumber() throws IllegalArgumentException, NotFoundException {

        when(phoneService.createPhoneNumber(userId, numberDTO)).thenReturn(numberDTO);

        ResponseEntity<?> response = phoneController.createPhoneNumber(userId, numberDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(numberDTO, response.getBody());
    }

    @Test
    void testUpdatePhoneNumber() throws NotFoundException, IllegalArgumentException {

        PhoneNumberDTO newPhone = new PhoneNumberDTO(1L, "Mob", "(21) 98966-2377", null);

        Set<PhoneNumberDTO> newPhonesDTO = new HashSet<>();
        newPhonesDTO.add(newPhone);

        when(phoneService.updatePhoneNumber(userId, newPhone)).thenReturn(newPhonesDTO);
        when(phoneService.getPhonesByUserId(userId)).thenReturn(newPhonesDTO);

        ResponseEntity<?> response = phoneController.updatePhoneNumber(userId, newPhone);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Set<PhoneNumberDTO> expectedPhones = (Set<PhoneNumberDTO>) response.getBody();
        assertTrue(expectedPhones.containsAll(newPhonesDTO));
    }

    @Test
    void testGetPhonesByUserId() throws NotFoundException {

        Set<PhoneNumberDTO> phoneSet = new HashSet<>();
        phoneSet.add(numberDTO);

        when(phoneService.getPhonesByUserId(userId)).thenReturn(phoneSet);

        ResponseEntity<?> response = phoneController.getPhonesByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Set<PhoneNumberDTO> expectedPhones = (Set<PhoneNumberDTO>) response.getBody();
        assertTrue(expectedPhones.containsAll(phoneSet));
    }

    @Test
    void testGetPhoneNumbers() throws NotFoundException {

        Set<PhoneNumberDTO> phoneSet = new HashSet<>();
        phoneSet.add(numberDTO);

        when(phoneService.getPhoneNumbers()).thenReturn(phoneSet);

        ResponseEntity<?> response = phoneController.getPhoneNumbers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Set<PhoneNumberDTO> expectedPhones = (Set<PhoneNumberDTO>) response.getBody();
        assertTrue(expectedPhones.containsAll(phoneSet));
        assertEquals(phoneSet, response.getBody());
    }

    @Test
    void testDeletePhoneNumber() throws NotFoundException {

        Set<PhoneNumberDTO> phoneSet = new HashSet<>();
        phoneSet.add(numberDTO);

        when(phoneService.getPhonesByUserId(userId)).thenReturn(phoneSet);

        ResponseEntity<?> response = phoneController.deletePhoneNumber(userId, numberDTO);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(phoneService, times(1)).deletePhoneNumber(userId, numberDTO);
    }

}
