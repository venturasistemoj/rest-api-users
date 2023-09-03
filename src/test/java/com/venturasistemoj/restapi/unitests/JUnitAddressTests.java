package com.venturasistemoj.restapi.unitests;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.venturasistemoj.restapi.domain.address.AddressController;
import com.venturasistemoj.restapi.domain.address.AddressDTO;
import com.venturasistemoj.restapi.domain.address.AddressService;
import com.venturasistemoj.restapi.domain.user.UserDTO;

public class JUnitAddressTests {

	@Mock
	private AddressService addressService;

	@InjectMocks
	private AddressController addressController;

	private UserDTO userDTO;
	private AddressDTO addressDTO;
	private final Long userId = 1L;

	@BeforeEach
	void setup() {

		// inicializa os campos anotados com @Mock e os injeta com @InjectMocks no controlador
		MockitoAnnotations.openMocks(this);

		userDTO = UserDTO.createSampleMaleUser(addressDTO);

		addressDTO = AddressDTO.createSampleAddress(userDTO);
	}

	@Test
	void testCreateAddress() throws IllegalArgumentException, NotFoundException {

		when(addressService.createAddress(userId, addressDTO)).thenReturn(addressDTO);

		ResponseEntity<?> response = addressController.createAddress(userId, addressDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(addressDTO, response.getBody());

	}

	@Test
	void testUpdateAddress() throws IllegalArgumentException, NotFoundException {

		UserDTO newUser = UserDTO.createSampleUser();

		AddressDTO newAddress = AddressDTO.createSampleAddress(userDTO);

		when(addressService.updateAddress(userId, newAddress)).thenReturn(newAddress);
		when(addressService.getAddressByUserId(userId)).thenReturn(newAddress);

		ResponseEntity<?> response = addressController.updateAddress(userId, newAddress);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(newAddress.streetAddress(), ((AddressDTO) response.getBody()).streetAddress());
		assertEquals(newAddress.state(), ((AddressDTO) response.getBody()).state());
		assertEquals(newAddress.zipCode(), ((AddressDTO) response.getBody()).zipCode());
		assertEquals(newAddress.userDTO(), ((AddressDTO) response.getBody()).userDTO());
	}

	void testGetAddressByUserId() throws NotFoundException {

		when(addressService.getAddressByUserId(userId)).thenReturn(addressDTO);

		ResponseEntity<?> response = addressController.getAddressByUserId(userId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(addressDTO, response.getBody());
	}

	@Test
	void testGetAdresses() throws NotFoundException {

		List<AddressDTO> adresses = new ArrayList<>();
		adresses.add(addressDTO);

		when(addressService.getAdresses()).thenReturn(adresses);

		ResponseEntity<?> response = addressController.getAdresses();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(adresses, response.getBody());

	}

	@Test
	void testDeleteAddress() throws NotFoundException {

		when(addressService.getAddressByUserId(userId)).thenReturn(addressDTO);

		ResponseEntity<?> response = addressController.deleteAddress(userId);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(addressService, times(1)).deleteAddress(userId);
	}

}
