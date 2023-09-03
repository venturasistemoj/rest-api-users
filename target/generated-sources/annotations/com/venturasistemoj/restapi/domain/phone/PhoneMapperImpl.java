package com.venturasistemoj.restapi.domain.phone;

import com.venturasistemoj.restapi.domain.address.AddressDTO;
import com.venturasistemoj.restapi.domain.user.User;
import com.venturasistemoj.restapi.domain.user.UserDTO;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-03T17:49:12-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.2.1 (Amazon.com Inc.)"
)
@Component
public class PhoneMapperImpl implements PhoneMapper {

    @Override
    public PhoneNumber phoneNumberDTOToPhoneNumber(PhoneNumberDTO phoneNumberDTO) {
        if ( phoneNumberDTO == null ) {
            return null;
        }

        PhoneNumber phoneNumber = new PhoneNumber();

        phoneNumber.setUser( userDTOToUser( phoneNumberDTO.userDTO() ) );
        phoneNumber.setPhoneId( phoneNumberDTO.phoneId() );
        phoneNumber.setType( phoneNumberDTO.type() );
        phoneNumber.setNumber( phoneNumberDTO.number() );

        return phoneNumber;
    }

    @Override
    public PhoneNumber phoneNumberDTOToEntity(PhoneNumberDTO phoneNumberDTO, User user) {
        if ( phoneNumberDTO == null ) {
            return null;
        }

        PhoneNumber phoneNumber = new PhoneNumber();

        phoneNumber.setPhoneId( phoneNumberDTO.phoneId() );
        phoneNumber.setType( phoneNumberDTO.type() );
        phoneNumber.setNumber( phoneNumberDTO.number() );

        setPhoneNumberUser( phoneNumber, user );

        return phoneNumber;
    }

    @Override
    public Set<PhoneNumber> phoneNumbersDTOToPhoneNumbers(Set<PhoneNumberDTO> phones) {
        if ( phones == null ) {
            return null;
        }

        Set<PhoneNumber> set = new LinkedHashSet<PhoneNumber>( Math.max( (int) ( phones.size() / .75f ) + 1, 16 ) );
        for ( PhoneNumberDTO phoneNumberDTO : phones ) {
            set.add( phoneNumberDTOToPhoneNumber( phoneNumberDTO ) );
        }

        return set;
    }

    @Override
    public PhoneNumberDTO phoneNumberToPhoneNumberDTO(PhoneNumber phoneNumber) {
        if ( phoneNumber == null ) {
            return null;
        }

        UserDTO userDTO = null;
        Long phoneId = null;
        String type = null;
        String number = null;

        userDTO = userToUserDTO( phoneNumber.getUser() );
        phoneId = phoneNumber.getPhoneId();
        type = phoneNumber.getType();
        number = phoneNumber.getNumber();

        PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO( phoneId, type, number, userDTO );

        return phoneNumberDTO;
    }

    @Override
    public Set<PhoneNumberDTO> phoneNumbersToPhoneNumbersDTO(Set<PhoneNumber> phoneNumbers) {
        if ( phoneNumbers == null ) {
            return null;
        }

        Set<PhoneNumberDTO> set = new LinkedHashSet<PhoneNumberDTO>( Math.max( (int) ( phoneNumbers.size() / .75f ) + 1, 16 ) );
        for ( PhoneNumber phoneNumber : phoneNumbers ) {
            set.add( phoneNumberToPhoneNumberDTO( phoneNumber ) );
        }

        return set;
    }

    protected User userDTOToUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setUserId( userDTO.userId() );
        user.setName( userDTO.name() );
        user.setSurName( userDTO.surName() );
        user.setBirthDate( userDTO.birthDate() );
        user.setCpf( userDTO.cpf() );
        user.setEmail( userDTO.email() );

        return user;
    }

    protected UserDTO userToUserDTO(User user) {
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
