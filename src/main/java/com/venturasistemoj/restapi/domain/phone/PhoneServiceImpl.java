package com.venturasistemoj.restapi.domain.phone;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.venturasistemoj.restapi.domain.user.User;
import com.venturasistemoj.restapi.domain.user.UserMapper;
import com.venturasistemoj.restapi.domain.user.UserRepository;

/**
 * [EN] Class implementing the PhoneService interface for managing business logic related to PhoneNumbers.
 *
 * [PT] Classe de implementação da interface PhoneService para gerenciar a lógica de negócios relacionada a telefones.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@Service
public class PhoneServiceImpl implements PhoneService {

	@Autowired private PhoneRepository phoneRepository;
	@Autowired private PhoneMapper phoneMapper;

	@Autowired private UserRepository userRepository;
	@Autowired private UserMapper userMapper;

	/**
	 * Cria um novo telefone para um usuário existente.
	 * Caso o usuário não exista, lança NotFoundException.
	 * Caso os dados do telefone estejam corrompidos ou sejam iguais,
	 * lança IllegalPhoneStateException e IllegalArgumentException, respectivamente.
	 * RN5: não é possível criar telefone sem usuário.
	 */
	@Override
	public PhoneNumberDTO createPhoneNumber(@NotNull Long userId, @Valid PhoneNumberDTO phoneDTO)
			throws NotFoundException, IllegalStateException, IllegalPhoneStateException {

		User existingUser = checkUser(userId); // RN5

		if(phoneDTO.getType() == null || phoneDTO.getNumber() == null)
			throw new IllegalPhoneStateException("Dados do telefone incompletos!");

		existingUser.getPhones().forEach(phone -> {
			if(phone.getNumber().equals(phoneDTO.getNumber()))
				throw new IllegalStateException("Número de telefone duplicado!");
		});

		phoneDTO.setUserDTO(userMapper.userToUserDTO(existingUser)); // associa usuário a telefone
		PhoneNumber savedPhoneNumber = phoneRepository.save(phoneMapper.phoneNumberDTOToPhoneNumber(phoneDTO));
		return phoneMapper.phoneNumberToPhoneNumberDTO(savedPhoneNumber);
	}

	/**
	 * Atualiza um telefone da lista de telefones de um usuário existente.
	 * Caso o usuário não exista, lança NotFoundException.
	 * Caso os dados do telefone estejam corrompidos, lança IllegalPhoneStateException.
	 * Retorna a lista de telefones atualizada.
	 */
	@Override
	@Transactional(rollbackFor = IllegalArgumentException.class)
	public List<PhoneNumberDTO> updatePhoneNumber(@NotNull Long userId, @Valid PhoneNumberDTO phoneDTO)
			throws NotFoundException, IllegalPhoneStateException {

		User existingUser = checkUser(userId);

		if( ! checkPhoneState(phoneDTO))
			throw new IllegalPhoneStateException("Dados do telefone incompletos!");

		List<PhoneNumber> userPhones = phoneRepository.findAllByUser(existingUser);

		if(userPhones.isEmpty())
			throw new NotFoundException();

		userPhones.forEach(phone -> {
			if(phone.getPhoneId().equals(phoneDTO.getPhoneId())
					&& ( ! phone.getNumber().equals(phoneDTO.getNumber())) ) {
				phone.setType(phoneDTO.getType());
				phone.setNumber(phoneDTO.getNumber());
			}
		});

		phoneRepository.saveAll(userPhones);

		return phoneMapper.phoneNumbersToPhoneNumbersDTO(userPhones);
	}

	/**
	 * Obtém a lista de telefones de um usuário existente.
	 * Caso o usuário não exista ou não haja telefones cadastrados, lança NotFoundException.
	 */
	@Override
	@Transactional(readOnly = true)
	public List<PhoneNumberDTO> getPhonesByUserId(@NotNull Long userId) throws NotFoundException {

		User existingUser = checkUser(userId);

		if( ! existingUser.getPhones().isEmpty())
			return phoneRepository.findAllByUser(existingUser)
					.stream()
					.map(phoneMapper::phoneNumberToPhoneNumberDTO)
					.collect(Collectors.toList());
		else
			throw new NotFoundException();
	}

	/**
	 * Obtém todos os telefones da base de dados.
	 * Caso não haja telefones cadastrados, lança NotFoundException.
	 */
	@Override
	@Transactional(readOnly = true)
	public List<PhoneNumberDTO> getPhoneNumbers() throws NotFoundException {

		if(phoneRepository.findAll().isEmpty())
			throw new NotFoundException();

		return phoneMapper.phoneNumbersToPhoneNumbersDTO(phoneRepository.findAll());
	}

	/**
	 * Remove um telefone da lista de telefones de um usuário existente.
	 * Caso o usuário não exista, lança NotFoundException.
	 * Caso os dados do telefone a ser removido estejam corrompidos, lança IllegalPhoneStateException.
	 */
	@Override
	@Transactional
	public void deletePhoneNumber(@NotNull Long userId, @Valid PhoneNumberDTO phoneDTO)
			throws NotFoundException, IllegalPhoneStateException {

		User existingUser = checkUser(userId);

		if( ! checkPhoneState(phoneDTO))
			throw new IllegalPhoneStateException("Dados do telefone incompletos!");

		PhoneNumber existingPhone = phoneRepository.findById(phoneDTO.getPhoneId()).orElseThrow(NotFoundException::new);

		existingUser.getPhones().remove(existingPhone); // desassocia telefone da lista do usuário
		phoneRepository.deleteById(existingPhone.getPhoneId()); // exclui o telefone somente deste usuário

	}

	// Verifica existência de usuário para associar telefone.
	@Transactional(readOnly = true)
	private User checkUser(Long userId) throws NotFoundException {

		Optional<User> optionalUser = userRepository.findById(userId);

		if(optionalUser.isPresent())
			return optionalUser.get();
		else
			throw new NotFoundException();
	}

	// verifica a consistência dos dados do telefone
	private boolean checkPhoneState(PhoneNumberDTO phoneDTO) {

		if(phoneDTO.getPhoneId() == null || phoneDTO.getType() == null || phoneDTO.getNumber() == null)
			return false;

		return true;
	}

}
