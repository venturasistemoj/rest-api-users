package com.venturasistemoj.restapi.domain.address;

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
public interface AddressMapper {

	@Mapping(target = "user", source = "userDTO")
	Address addressDTOToAddress(AddressDTO addressDTO);

	@InheritInverseConfiguration
	AddressDTO addressToAddressDTO(Address address);

	@IterableMapping(elementTargetType = Address.class)
	List<Address> adressesDTOToAdresses(List<AddressDTO> adressesDTO);

	@InheritInverseConfiguration
	List<AddressDTO> adressesToAdressesDTO(List<Address> adresses);
}
