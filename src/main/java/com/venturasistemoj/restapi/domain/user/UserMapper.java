package com.venturasistemoj.restapi.domain.user;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.venturasistemoj.restapi.domain.address.AddressMapper;
import com.venturasistemoj.restapi.domain.phone.PhoneMapper;

/**
 * Interface used for mapping entities to DTOs and vice versa by MapStruct.
 *
 * <p>The <code>@Mapper</code> annotation marks an interface or abstract class as a mapper and activates the generation
 * of a implementation of that type via MapStruct.</p>
 * <p>The <code>componentModel</code> parameter defines how the mapper will be managed and injected into other project
 * components. The component model is Spring and injection into the service is done at the field level with
 * <code>@Autowired</code>.</p>
 * <p>The <code>unmappedTargetPolicy</code> parameter defines the default reporting policy to apply if an attribute
 * of a mapping method's target object is not populated with a source value. <code>ReportingPolicy.IGNORE</code>
 * avoids generating errors for unmapped properties./<p>
 * <p>The <code>uses</code> parameter specifies other mappers used as dependencies on the current mapper.</p>
 * <p>The <code>@InheritInverseConfiguration</code> annotation inherits the <strong>inverse mapping</strong> configuration
 * from the respective method method.</p>
 * <p><code>@IterableMapping</code> configures the mapping between two similar iterable types, for example
 * <code>List<User></code> and <code>List<UserDTO></code>./<p>
 *
 * @author Wilson Ventura
 */
@Mapper(
		componentModel = "spring",
		unmappedTargetPolicy = ReportingPolicy.IGNORE,
		uses = { AddressMapper.class, PhoneMapper.class }
		)
public interface UserMapper {

	@Mapping(target = "address", source = "addressDTO")
	@Mapping(target = "phones", source = "phonesDTO")
	User userDTOToUser(UserDTO userDTO);

	@InheritInverseConfiguration
	UserDTO userToUserDTO(User user);

	@IterableMapping(elementTargetType = User.class)
	List<User> usersDTOToUsers(List<UserDTO> users);

	@InheritInverseConfiguration
	List<UserDTO> usersToUsersDTO(List<User> users);
}
