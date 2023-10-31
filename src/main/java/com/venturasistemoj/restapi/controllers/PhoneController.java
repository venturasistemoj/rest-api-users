package com.venturasistemoj.restapi.controllers;

import java.util.Set;

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

import com.venturasistemoj.restapi.domain.phone.PhoneNumberDTO;
import com.venturasistemoj.restapi.domain.phone.PhoneService;
import com.venturasistemoj.restapi.exceptions.IllegalPhoneStateException;

/**
 * Phone Numbers API interface for coordinating requests and responses.
 *
 * <p>The Spring <code>@RestController</code> annotation marks the class as a controller where each method returns
 * a domain object instead of a view. It is a shortcut to include <code>@Controller</code> and
 * <code>@ResponseBody/<code> Java annotations.</p>
 *
 * @author Wilson Ventura
 */

@RestController
@RequestMapping("/rest-api/phones")
public class PhoneController {

	@Autowired
	private PhoneService phoneService;

	private static final String NOT_FOUND = "Nonexistent user or phone number!";

	@PostMapping("/{userId}")
	public ResponseEntity<?> createPhoneNumber(@PathVariable Long userId, @RequestBody PhoneNumberDTO phoneDTO) {

		try {
			PhoneNumberDTO savedPhoneNumber = phoneService.createPhoneNumber(userId, phoneDTO);
			// Spring RESTful web service controller populates and returns an object.
			// The object data will be written directly to the HTTP response as JSON by embedded Jackson JSON.
			return ResponseEntity.status(HttpStatus.CREATED).body(savedPhoneNumber);
		} catch (IllegalPhoneStateException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND);
		}
	}

	@PutMapping("/{userId}")
	public ResponseEntity<?> updatePhoneNumber(@PathVariable Long userId, @RequestBody PhoneNumberDTO phoneDTO) {
		try {
			Set<PhoneNumberDTO> updatedPhones = phoneService.updatePhoneNumber(userId, phoneDTO);
			return ResponseEntity.ok(updatedPhones);
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND);
		} catch (IllegalPhoneStateException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping("/{userId}")
	public ResponseEntity<?> getPhonesByUserId(@PathVariable Long userId) {

		try {
			Set<PhoneNumberDTO> phones = phoneService.getPhonesByUserId(userId);
			return ResponseEntity.ok(phones);
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND);
		}
	}

	@GetMapping
	public ResponseEntity<?> getPhoneNumbers() {

		try {
			return ResponseEntity.ok(phoneService.getPhoneNumbers());
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND);
		}
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deletePhoneNumber(@PathVariable Long userId, @RequestBody PhoneNumberDTO phoneDTO) {

		try {
			phoneService.deletePhoneNumber(userId, phoneDTO);
			return ResponseEntity.noContent().build();
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND);
		}  catch (IllegalPhoneStateException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

}
