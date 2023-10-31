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

import com.venturasistemoj.restapi.controllers.AddressController;
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
	private final Long ID = 1L;

	@BeforeEach
	void setup() {

		// initializes fields annotated with @Mock and injects them with @InjectMocks into the controller
		MockitoAnnotations.openMocks(this);

		userDTO = UserDTO.builder()
				.userId(ID)
				.name("Luiz Inacio")
				.surName("da Silva")
				.birthDate(LocalDate.of(1972, Month.FEBRUARY, 22))
				.cpf("123.456.789-10")
				.email("lula@prov.com")
				.build();

		addressDTO = AddressDTO.builder()
				.addressId(ID)
				.publicPlace("Avenida")
				.streetAddress("Glasshouse, 69")
				.complement("1001")
				.city("Rio 40º")
				.state("RJ")
				.zipCode("69.069-069")
				.userDTO(userDTO) // assigns user to address
				.build();
	}

	@Test
	void testCreateAddress() throws IllegalArgumentException, NotFoundException {

		when(addressService.createAddress(ID, addressDTO)).thenReturn(addressDTO);

		ResponseEntity<?> response = addressController.createAddress(ID, addressDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(addressDTO, response.getBody());

	}

	@Test
	void testUpdateAddress() throws IllegalArgumentException, NotFoundException {

		AddressDTO newAddress = AddressDTO.builder()
				.addressId(ID)
				.publicPlace("Avenida")
				.streetAddress("Planalto, 69")
				.complement("1010")
				.city("Rio")
				.state("RJ")
				.zipCode("69.069-069")
				.build();

		when(addressService.updateAddress(ID, newAddress)).thenReturn(newAddress);
		when(addressService.getAddressByUserId(ID)).thenReturn(newAddress);

		ResponseEntity<?> response = addressController.updateAddress(ID, newAddress);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(newAddress.getPublicPlace(), ((AddressDTO) response.getBody()).getPublicPlace());
		assertEquals(newAddress.getStreetAddress(), ((AddressDTO) response.getBody()).getStreetAddress());
		assertEquals(newAddress.getCity(), ((AddressDTO) response.getBody()).getCity());
		assertEquals(newAddress.getState(), ((AddressDTO) response.getBody()).getState());
		assertEquals(newAddress.getZipCode(), ((AddressDTO) response.getBody()).getZipCode());
		assertEquals(newAddress.getUserDTO(), ((AddressDTO) response.getBody()).getUserDTO());
	}

	void testGetAddressByUserId() throws NotFoundException {

		when(addressService.getAddressByUserId(ID)).thenReturn(addressDTO);

		ResponseEntity<?> response = addressController.getAddressByUserId(ID);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(addressDTO, response.getBody());
	}

	@Test
	void testGetAdresses() throws NotFoundException {

		List<AddressDTO> allAdresses = new ArrayList<>();
		allAdresses.add(addressDTO);

		when(addressService.getAdresses()).thenReturn(allAdresses);

		ResponseEntity<?> response = addressController.getAdresses();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(allAdresses, response.getBody());

	}

	@Test
	void testDeleteAddress() throws NotFoundException {

		when(addressService.getAddressByUserId(ID)).thenReturn(addressDTO);

		ResponseEntity<?> response = addressController.deleteAddress(ID);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(addressService, times(1)).deleteAddress(ID);
	}

}
