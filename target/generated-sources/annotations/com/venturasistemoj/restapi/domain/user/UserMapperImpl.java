package com.venturasistemoj.restapi.domain.user;

import com.venturasistemoj.restapi.domain.address.AddressDTO;
import com.venturasistemoj.restapi.domain.address.AddressMapper;
import com.venturasistemoj.restapi.domain.phone.PhoneMapper;
import com.venturasistemoj.restapi.domain.phone.PhoneNumberDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-05T12:53:04-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.2.1 (Amazon.com Inc.)"
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

        user.setAddress( addressMapper.addressDTOToAddress( userDTO.addressDTO() ) );
        user.setPhones( phoneMapper.phoneNumbersDTOToPhoneNumbers( userDTO.phonesDTO() ) );
        user.setUserId( userDTO.userId() );
        user.setName( userDTO.name() );
        user.setSurName( userDTO.surName() );
        user.setBirthDate( userDTO.birthDate() );
        user.setCpf( userDTO.cpf() );
        user.setEmail( userDTO.email() );

        return user;
    }

    @Override
    public UserDTO userToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        AddressDTO addressDTO = null;
        Set<PhoneNumberDTO> phonesDTO = null;
        Long userId = null;
        String name = null;
        String surName = null;
        LocalDate birthDate = null;
        String cpf = null;
        String email = null;

        addressDTO = addressMapper.addressToAddressDTO( user.getAddress() );
        phonesDTO = phoneMapper.phoneNumbersToPhoneNumbersDTO( user.getPhones() );
        userId = user.getUserId();
        name = user.getName();
        surName = user.getSurName();
        birthDate = user.getBirthDate();
        cpf = user.getCpf();
        email = user.getEmail();

        UserDTO userDTO = new UserDTO( userId, name, surName, birthDate, cpf, email, addressDTO, phonesDTO );

        return userDTO;
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

        Long userId = null;
        String name = null;
        String surName = null;
        LocalDate birthDate = null;
        String cpf = null;
        String email = null;

        userId = user.getUserId();
        name = user.getName();
        surName = user.getSurName();
        birthDate = user.getBirthDate();
        cpf = user.getCpf();
        email = user.getEmail();

        AddressDTO addressDTO = null;
        Set<PhoneNumberDTO> phonesDTO = null;

        UserDTO userDTO = new UserDTO( userId, name, surName, birthDate, cpf, email, addressDTO, phonesDTO );

        return userDTO;
    }
}
