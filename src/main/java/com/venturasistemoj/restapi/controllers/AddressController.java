package com.venturasistemoj.restapi.controllers;

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

import com.venturasistemoj.restapi.domain.address.AddressDTO;
import com.venturasistemoj.restapi.domain.address.AddressService;
import com.venturasistemoj.restapi.exceptions.IllegalAddressStateException;
import com.venturasistemoj.restapi.exceptions.IllegalOperationException;

/**
 * Adresses API interface for coordinating requests and responses.
 *
 * <p>The Spring <code>@RestController</code> annotation marks the class as a controller where each method returns
 * a domain object instead of a view. It is a shortcut to include <code>@Controller</code> and
 * <code>@ResponseBody/<code> Java annotations.</p>
 *
 * @author Wilson Ventura
 */

@RestController
@RequestMapping("/rest-api/adresses")
public class AddressController {

	@Autowired
	private AddressService addressService;

	private static final String NOT_FOUND = "Nonexistent user or address!";

	@PostMapping("/{userId}")
	public ResponseEntity<?> createAddress(@PathVariable Long userId, @RequestBody AddressDTO addressDTO) {

		try {
			AddressDTO savedAddress = addressService.createAddress(userId, addressDTO);
			// Spring RESTful web service controller populates and returns an object.
			// The object data will be written directly to the HTTP response as JSON by embedded Jackson JSON.
			return ResponseEntity.status(HttpStatus.CREATED).body(savedAddress);
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND);
		} catch (IllegalOperationException | IllegalAddressStateException e) {
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
