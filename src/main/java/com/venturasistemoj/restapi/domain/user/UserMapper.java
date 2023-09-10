package com.venturasistemoj.restapi.domain.user;

import java.util.List;

import com.venturasistemoj.restapi.domain.phone.PhoneNumber;
import org.mapstruct.*;

import com.venturasistemoj.restapi.domain.address.AddressMapper;
import com.venturasistemoj.restapi.domain.phone.PhoneMapper;

/**
 * [EN] Interface used for mapping entities to DTOs and vice versa by MapStruct.
 *
 * [PT] Interface usada pelo MapStruct para mapear entidades para DTOs e vice-versa.
 *
 * O parâmetro <code>componentModel</code> define como o mapeador será gerenciado e injetado em outros componentes
 * do projeto. O modelo do componente é Spring e a injeção no serviço é feita em nível de campo com
 * <code>@Autowired.</code>>
 * O parâmetro <code>unmappedTargetPolicy</code> define a política de relatórios padrão a ser aplicada se um atributo
 * do objeto destino de um método de mapeamento não é preenchido com um valor de origem. <code>ReportingPolicy.IGNORE</code>
 * evita gerar erros para propriedades não mapeadas.
 * O parâmetro <code>uses</code> especifica outros mapeadores usados como dependências no mapeador atual.
 * A anotação <code>@InheritInverseConfiguration</code> herda a configuração de <strong>mapeamento inverso</strong> do
 * método <code>userDTOToUser</code>.
 * <code>@IterableMapping</code> configura o mapeamento entre dois tipos semelhantes iteráveis,
 * por exemplo List<Usuario> e List<UsuarioDTO>.
 *
 * @author Wilson Ventura
 * @since 2023
 */
@Mapper
public interface UserMapper {

	@Mapping(target = "address", source = "addressDTO")
	@Mapping(target = "phones", source = "phonesDTO")
	User userDTOToUser(UserDTO userDTO);

	@AfterMapping
	private void afterMapping(UserDTO userDTO, @MappingTarget User user) {
		if (user.getAddress() != null)
			user.getAddress().setUser(user);
		if(user.getPhones() != null && user.getPhones().size() > 0)
			user.getPhones().stream().forEach(x -> x.setUser(user));
	}

}
