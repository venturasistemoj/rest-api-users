package com.venturasistemoj.restapi.domain.phone;

import java.util.Set;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Interface used for mapping entities to DTOs and vice versa by MapStruct.
 *
 * <p>The <code>@Mapper</code> annotation marks an interface or abstract class as a mapper and activates the generation
 * of a implementation of that type via MapStruct./<p>
 * <p>The <code>componentModel</code> parameter defines how the mapper will be managed and injected into other project
 * components. The component model is Spring and injection into the service is done at the field level with
 * <code>@Autowired</code>./<p>
 * <p>The <code>unmappedTargetPolicy</code> parameter defines the default reporting policy to apply if an attribute
 * of a mapping method's target object is not populated with a source value. <code>ReportingPolicy.IGNORE</code>
 * avoids generating errors for unmapped properties./<p>
 *
 * @author Wilson Ventura
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhoneMapper {

	@Mapping(target = "user", source = "userDTO")
	PhoneNumber phoneNumberDTOToPhoneNumber(PhoneNumberDTO phoneNumberDTO);

	@InheritInverseConfiguration
	PhoneNumberDTO phoneNumberToPhoneNumberDTO(PhoneNumber phoneNumber);

	@IterableMapping(elementTargetType = PhoneNumber.class)
	Set<PhoneNumber> phoneNumbersDTOToPhoneNumbers(Set<PhoneNumberDTO> phones);

	@InheritInverseConfiguration
	Set<PhoneNumberDTO> phoneNumbersToPhoneNumbersDTO(Set<PhoneNumber> phones);
}
