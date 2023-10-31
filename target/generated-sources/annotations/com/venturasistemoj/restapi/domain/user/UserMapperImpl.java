package com.venturasistemoj.restapi.domain.user;

import com.venturasistemoj.restapi.domain.address.AddressMapper;
import com.venturasistemoj.restapi.domain.phone.PhoneMapper;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-31T13:24:32-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.33.0.v20230218-1114, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private PhoneMapper phoneMapper;

    @Override
    public User userDTOToUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setAddress( addressMapper.addressDTOToAddress( userDTO.getAddressDTO() ) );
        user.setPhones( phoneMapper.phoneNumbersDTOToPhoneNumbers( userDTO.getPhonesDTO() ) );
        user.setBirthDate( userDTO.getBirthDate() );
        user.setCpf( userDTO.getCpf() );
        user.setEmail( userDTO.getEmail() );
        user.setName( userDTO.getName() );
        user.setSurName( userDTO.getSurName() );
        user.setUserId( userDTO.getUserId() );

        return user;
    }

    @Override
    public UserDTO userToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.addressDTO( addressMapper.addressToAddressDTO( user.getAddress() ) );
        userDTO.phonesDTO( phoneMapper.phoneNumbersToPhoneNumbersDTO( user.getPhones() ) );
        userDTO.birthDate( user.getBirthDate() );
        userDTO.cpf( user.getCpf() );
        userDTO.email( user.getEmail() );
        userDTO.name( user.getName() );
        userDTO.surName( user.getSurName() );
        userDTO.userId( user.getUserId() );

        return userDTO.build();
    }

    @Override
    public List<User> usersDTOToUsers(List<UserDTO> users) {
        if ( users == null ) {
            return null;
        }

        List<User> list = new ArrayList<User>( users.size() );
        for ( UserDTO userDTO : users ) {
            list.add( userDTOToUser( userDTO ) );
        }

        return list;
    }

    @Override
    public List<UserDTO> usersToUsersDTO(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>( users.size() );
        for ( User user : users ) {
            list.add( userToUserDTO1( user ) );
        }

        return list;
    }

    protected UserDTO userToUserDTO1(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.birthDate( user.getBirthDate() );
        userDTO.cpf( user.getCpf() );
        userDTO.email( user.getEmail() );
        userDTO.name( user.getName() );
        userDTO.surName( user.getSurName() );
        userDTO.userId( user.getUserId() );

        return userDTO.build();
    }
}
