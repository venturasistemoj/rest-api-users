package com.venturasistemoj.restapi.domain.phone;

import com.venturasistemoj.restapi.domain.user.User;
import com.venturasistemoj.restapi.domain.user.UserDTO;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-31T13:24:32-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.33.0.v20230218-1114, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class PhoneMapperImpl implements PhoneMapper {

    @Override
    public PhoneNumber phoneNumberDTOToPhoneNumber(PhoneNumberDTO phoneNumberDTO) {
        if ( phoneNumberDTO == null ) {
            return null;
        }

        PhoneNumber phoneNumber = new PhoneNumber();

        phoneNumber.setUser( userDTOToUser( phoneNumberDTO.getUserDTO() ) );
        phoneNumber.setNumber( phoneNumberDTO.getNumber() );
        phoneNumber.setPhoneId( phoneNumberDTO.getPhoneId() );
        phoneNumber.setType( phoneNumberDTO.getType() );

        return phoneNumber;
    }

    @Override
    public PhoneNumberDTO phoneNumberToPhoneNumberDTO(PhoneNumber phoneNumber) {
        if ( phoneNumber == null ) {
            return null;
        }

        PhoneNumberDTO.PhoneNumberDTOBuilder phoneNumberDTO = PhoneNumberDTO.builder();

        phoneNumberDTO.userDTO( userToUserDTO( phoneNumber.getUser() ) );
        phoneNumberDTO.number( phoneNumber.getNumber() );
        phoneNumberDTO.phoneId( phoneNumber.getPhoneId() );
        phoneNumberDTO.type( phoneNumber.getType() );

        return phoneNumberDTO.build();
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
    public Set<PhoneNumberDTO> phoneNumbersToPhoneNumbersDTO(Set<PhoneNumber> phones) {
        if ( phones == null ) {
            return null;
        }

        Set<PhoneNumberDTO> set = new LinkedHashSet<PhoneNumberDTO>( Math.max( (int) ( phones.size() / .75f ) + 1, 16 ) );
        for ( PhoneNumber phoneNumber : phones ) {
            set.add( phoneNumberToPhoneNumberDTO1( phoneNumber ) );
        }

        return set;
    }

    protected User userDTOToUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setBirthDate( userDTO.getBirthDate() );
        user.setCpf( userDTO.getCpf() );
        user.setEmail( userDTO.getEmail() );
        user.setName( userDTO.getName() );
        user.setSurName( userDTO.getSurName() );
        user.setUserId( userDTO.getUserId() );

        return user;
    }

    protected UserDTO userToUserDTO(User user) {
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

    protected PhoneNumberDTO phoneNumberToPhoneNumberDTO1(PhoneNumber phoneNumber) {
        if ( phoneNumber == null ) {
            return null;
        }

        PhoneNumberDTO.PhoneNumberDTOBuilder phoneNumberDTO = PhoneNumberDTO.builder();

        phoneNumberDTO.number( phoneNumber.getNumber() );
        phoneNumberDTO.phoneId( phoneNumber.getPhoneId() );
        phoneNumberDTO.type( phoneNumber.getType() );

        return phoneNumberDTO.build();
    }
}
