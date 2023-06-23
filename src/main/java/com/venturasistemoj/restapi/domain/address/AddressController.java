package com.venturasistemoj.restapi.domain.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [EN] Address API interface for coordinating requests and responses.
 *
 * [PT] Interface da API de endereços para coordenar requisições e respostas.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@RestController
@RequestMapping("/rest-api/adresses")
public class AddressController {

	@Autowired
	private AddressService addressService;

	private static final String BAD_ADDRESS = "Dados do endereço duplicados!";
	private static final String NOT_FOUND = "Usuário ou endereço inexistente!";
	private static final String EXISTING_ADDRESS = "Usuário já possui endereço cadastrado!";

	@PostMapping("/{userId}")
	public ResponseEntity<?> createAddress(@PathVariable Long userId, @RequestBody AddressDTO addressDTO) {

		try {
			AddressDTO savedAddress = addressService.createAddress(userId, addressDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedAddress);
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND);
		} catch (IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(EXISTING_ADDRESS);
		} catch (IllegalAddressStateException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PutMapping("/{userId}")
	public ResponseEntity<?> updateAddress(@PathVariable Long userId, @RequestBody AddressDTO addressDTO) {

		try {
			AddressDTO updatedAddress = addressService.updateAddress(userId, addressDTO);
			return ResponseEntity.ok(updatedAddress);
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BAD_ADDRESS);
		} catch (IllegalAddressStateException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping("/{userId}")
	public ResponseEntity<?> getAddressByUserId(@PathVariable Long userId) {

		try {
			AddressDTO existingAddress = addressService.getAddressByUserId(userId);
			return ResponseEntity.ok(existingAddress);
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND);
		}
	}

	@GetMapping
	public ResponseEntity<?> getAdresses() {

		try {
			return ResponseEntity.ok(addressService.getAdresses());
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND);
		}
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteAddress(@PathVariable Long userId) {

		try {
			addressService.deleteAddress(userId);
			return ResponseEntity.noContent().build();
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND);
		}
	}

}
