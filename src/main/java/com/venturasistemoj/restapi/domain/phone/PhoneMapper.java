package com.venturasistemoj.restapi.domain.phone;

import java.util.Set;
import java.util.stream.Collectors;

import com.venturasistemoj.restapi.domain.address.Address;
import com.venturasistemoj.restapi.domain.address.AddressDTO;
import com.venturasistemoj.restapi.domain.user.User;
import com.venturasistemoj.restapi.domain.user.UserDTO;
import org.mapstruct.*;

/**
 * [EN] Interface used for mapping entities to DTOs and vice versa by MapStruct.
 *
 * [PT] Interface usada pelo MapStruct para mapear entidades para DTOs e vice-versa.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhoneMapper {

	@Mapping(target = "user", source = "userDTO")
	PhoneNumber phoneNumberDTOToPhoneNumber(PhoneNumberDTO phoneNumberDTO);

	PhoneNumber phoneNumberDTOToEntity(PhoneNumberDTO phoneNumberDTO, @Context User user);
	@AfterMapping
	default void setPhoneNumberUser(@MappingTarget PhoneNumber phoneNumber, @Context User user) {
		if (user != null) {
			phoneNumber.setUser(user);
		}
	}

	@IterableMapping(elementTargetType = PhoneNumber.class)
	Set<PhoneNumber> phoneNumbersDTOToPhoneNumbers(Set<PhoneNumberDTO> phones);

	@Mapping(target = "userDTO", source = "user")
	PhoneNumberDTO phoneNumberToPhoneNumberDTO(PhoneNumber phoneNumber);

	Set<PhoneNumberDTO> phoneNumbersToPhoneNumbersDTO(Set<PhoneNumber> phoneNumbers);

	@Named("mapUserDTOToEntity")
	default User mapUserDTOToEntity(UserDTO userDTO) {
		if (userDTO == null) {
			return null;
		}
		User user = new User();
		user.setUserId(userDTO.userId());
		user.setName(userDTO.name());
		user.setSurName(userDTO.surName());
		user.setBirthDate(userDTO.birthDate());
		user.setCpf(userDTO.cpf());
		user.setEmail(userDTO.email());

		// Map the AddressDTO to Address if needed
		if (userDTO.addressDTO() != null) {
			Address address = mapAddressDTOToEntity(userDTO.addressDTO());
			address.setUser(user);
			user.setAddress(address);
		}

		// Map the Set<PhoneNumberDTO> to Set<PhoneNumber> if needed
		if (userDTO.phonesDTO() != null) {
			Set<PhoneNumber> phoneNumbers = mapPhoneDTOsToEntities(userDTO.phonesDTO());
			phoneNumbers.forEach(phone -> phone.setUser(user));
			user.setPhones(phoneNumbers);
		}

		return user;
	}
	@Named("mapPhoneDTOsToEntities")
	default Set<PhoneNumber> mapPhoneDTOsToEntities(Set<PhoneNumberDTO> phoneDTOs) {
		if (phoneDTOs == null) {
			return null;
		}

		return phoneDTOs.stream()
				.map(this::mapPhoneNumberDTOToEntity)
				.collect(Collectors.toSet());
	}

	@Named("mapPhoneNumberDTOToEntity")
	default PhoneNumber mapPhoneNumberDTOToEntity(PhoneNumberDTO phoneNumberDTO) {
		if (phoneNumberDTO == null) {
			return null;
		}

		PhoneNumber phoneNumber = new PhoneNumber();
		phoneNumber.setPhoneId(phoneNumberDTO.phoneId());
		phoneNumber.setType(phoneNumberDTO.type());
		phoneNumber.setNumber(phoneNumberDTO.number());

		// You may need to map other properties if present in PhoneNumberDTO

		return phoneNumber;
	}


	@Named("mapUserEntityToDTO")
	default UserDTO mapUserEntityToDTO(User user) {
		if (user == null) {
			return null;
		}
		UserDTO userDTO = new UserDTO(
				user.getUserId(),
				user.getName(),
				user.getSurName(),
				user.getBirthDate(),
				user.getCpf(),
				user.getEmail(),
				mapAddressEntityToDTO(user.getAddress()),
				mapPhoneEntitiesToDTOs(user.getPhones())
		);

		return userDTO;
	}

	@Named("mapPhoneNumberEntityToDTO")
	default PhoneNumberDTO mapPhoneNumberEntityToDTO(PhoneNumber phoneNumber) {
		if (phoneNumber == null) {
			return null;
		}

		PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO(
				phoneNumber.getPhoneId(),
				phoneNumber.getType(),
				phoneNumber.getNumber(),
				null
		);

		return phoneNumberDTO;
	}

	@Named("mapPhoneEntitiesToDTOs")
	default Set<PhoneNumberDTO> mapPhoneEntitiesToDTOs(Set<PhoneNumber> phoneEntities) {
		if (phoneEntities == null) {
			return null;
		}

		return phoneEntities.stream()
				.map(this::mapPhoneNumberEntityToDTO)
				.collect(Collectors.toSet());
	}


	@Named("mapAddressEntityToDTO")
	default AddressDTO mapAddressEntityToDTO(Address address) {
		if (address == null) {
			return null;
		}

		AddressDTO addressDTO = new AddressDTO(
				address.getAddressId(),
				address.getPublicPlace(),
				address.getStreetAddress(),
				address.getComplement(),
				address.getCity(),
				address.getState(),
				address.getZipCode(),
				null
		);

		return addressDTO;
	}


	@Named("mapAddressDTOToEntity")
	default Address mapAddressDTOToEntity(AddressDTO addressDTO) {
		if (addressDTO == null) {
			return null;
		}
		Address address = new Address();
		address.setAddressId(addressDTO.addressId());
		address.setPublicPlace(addressDTO.publicPlace());
		address.setStreetAddress(addressDTO.streetAddress());
		address.setComplement(addressDTO.complement());
		address.setCity(addressDTO.city());
		address.setState(addressDTO.state());
		address.setZipCode(addressDTO.zipCode());

		return address;
	}



}
