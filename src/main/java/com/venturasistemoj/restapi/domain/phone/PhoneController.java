package com.venturasistemoj.restapi.domain.phone;

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

/**
 * [EN] Phone Numbers API interface for coordinating requests and responses.
 *
 * [PT] Interface da API de telefones para coordenar requisições e respostas.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@RestController
@RequestMapping("/rest-api/phones")
public class PhoneController {

	@Autowired
	private PhoneService phoneService;

	private static final String BAD_PHONE = "Dados do telefone incorretos ou duplicados!";
	private static final String NOT_FOUND = "Usuário ou telefone(s) inexistente!";

	@PostMapping("/{userId}")
	public ResponseEntity<?> createPhoneNumber(@PathVariable Long userId, @RequestBody PhoneNumberDTO phoneDTO) {

		try {
			PhoneNumberDTO savedPhoneNumber = phoneService.createPhoneNumber(userId, phoneDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedPhoneNumber);
		} catch (IllegalPhoneStateException | IllegalStateException e) {
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
		} catch (IllegalPhoneStateException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BAD_PHONE);
		}
	}

}
