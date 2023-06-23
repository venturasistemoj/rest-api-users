package com.venturasistemoj.restapi.domain.phone;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

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

	@InheritInverseConfiguration
	PhoneNumberDTO phoneNumberToPhoneNumberDTO(PhoneNumber phoneNumber);

	@IterableMapping(elementTargetType = PhoneNumber.class)
	List<PhoneNumber> phoneNumbersDTOToPhoneNumbers(List<PhoneNumberDTO> phones);

	@InheritInverseConfiguration
	List<PhoneNumberDTO> phoneNumbersToPhoneNumbersDTO(List<PhoneNumber> phones);
}
