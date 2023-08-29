package com.venturasistemoj.restapi.domain.phone;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.venturasistemoj.restapi.domain.user.User;
import com.venturasistemoj.restapi.domain.user.UserMapper;
import com.venturasistemoj.restapi.domain.user.UserRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

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

	private static final String INCONPLETE_PHONE_DATA = "Dados do telefone incompletos!";

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
			throw new IllegalPhoneStateException(INCONPLETE_PHONE_DATA);

		/* O seguinte idioma, assim como a classe PhoneComparator, tornaram-se desnecessários com
		 * a substituição da estrutura de dados dos telefones da API de List para Set*/

		//if (existingUser.getPhones().contains(phoneMapper.phoneNumberDTOToPhoneNumber(phoneDTO)))
		//throw new IllegalStateException("Número de telefone duplicado!");

		phoneDTO.setUserDTO(userMapper.userToUserDTO(existingUser)); // associa usuário a telefone
		PhoneNumber savedPhoneNumber = phoneRepository.save(phoneMapper.phoneNumberDTOToPhoneNumber(phoneDTO));
		return phoneMapper.phoneNumberToPhoneNumberDTO(savedPhoneNumber);
	}

	/**
	 * Atualiza um telefone no conjunto de telefones de um usuário existente.
	 * Caso o usuário não exista, lança NotFoundException.
	 * Caso os dados do telefone estejam corrompidos, lança IllegalPhoneStateException.
	 * Retorna a lista de telefones atualizada.
	 */
	@Override
	@Transactional(rollbackFor = IllegalArgumentException.class)
	public Set<PhoneNumberDTO> updatePhoneNumber(@NotNull Long userId, @Valid PhoneNumberDTO phoneDTO)
			throws NotFoundException, IllegalPhoneStateException {

		if( ! checkPhoneState(phoneDTO))
			throw new IllegalPhoneStateException(INCONPLETE_PHONE_DATA);

		User existingUser = checkUser(userId);
		Set<PhoneNumber> userPhones = phoneRepository.findAllByUser(existingUser);

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
	 * Obtém o conjunto de telefones de um usuário existente.
	 * Caso o usuário não exista ou não haja telefones cadastrados, lança NotFoundException.
	 */
	@Override
	@Transactional(readOnly = true)
	public Set<PhoneNumberDTO> getPhonesByUserId(@NotNull Long userId) throws NotFoundException {

		User existingUser = checkUser(userId);

		if( ! existingUser.getPhones().isEmpty())
			return phoneRepository.findAllByUser(existingUser)
					.stream()
					.map(phoneMapper::phoneNumberToPhoneNumberDTO)
					.collect(Collectors.toSet());
		else
			throw new NotFoundException();
	}

	/**
	 * Obtém todos os telefones da base de dados.
	 * Caso não haja telefones cadastrados, lança NotFoundException.
	 */
	@Override
	@Transactional(readOnly = true)
	public Set<PhoneNumberDTO> getPhoneNumbers() throws NotFoundException {

		Set<PhoneNumber> allPhones = phoneRepository.findAll()
				.stream()
				.collect(Collectors.toSet());

		if(allPhones.isEmpty())
			throw new NotFoundException();

		return phoneMapper.phoneNumbersToPhoneNumbersDTO(allPhones);
	}

	/**
	 * Remove um telefone do conjunto de telefones de um usuário existente.
	 * Caso o usuário não exista, lança NotFoundException.
	 * Caso os dados do telefone a ser removido estejam corrompidos, lança IllegalPhoneStateException.
	 */
	@Override
	@Transactional
	public void deletePhoneNumber(@NotNull Long userId, @Valid PhoneNumberDTO phoneDTO)
			throws NotFoundException, IllegalPhoneStateException {

		PhoneNumber existingPhone = phoneRepository.findById(phoneDTO.getPhoneId()).orElseThrow(NotFoundException::new);

		User existingUser = checkUser(userId);

		if( ! checkPhoneState(phoneDTO))
			throw new IllegalPhoneStateException(INCONPLETE_PHONE_DATA);

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
