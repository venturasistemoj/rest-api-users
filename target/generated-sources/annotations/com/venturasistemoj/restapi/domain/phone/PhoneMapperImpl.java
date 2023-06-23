package com.venturasistemoj.restapi.domain.phone;

import com.venturasistemoj.restapi.domain.user.User;
import com.venturasistemoj.restapi.domain.user.UserDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-23T12:06:45-0300",
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
    public List<PhoneNumber> phoneNumbersDTOToPhoneNumbers(List<PhoneNumberDTO> phones) {
        if ( phones == null ) {
            return null;
        }

        List<PhoneNumber> list = new ArrayList<PhoneNumber>( phones.size() );
        for ( PhoneNumberDTO phoneNumberDTO : phones ) {
            list.add( phoneNumberDTOToPhoneNumber( phoneNumberDTO ) );
        }

        return list;
    }

    @Override
    public List<PhoneNumberDTO> phoneNumbersToPhoneNumbersDTO(List<PhoneNumber> phones) {
        if ( phones == null ) {
            return null;
        }

        List<PhoneNumberDTO> list = new ArrayList<PhoneNumberDTO>( phones.size() );
        for ( PhoneNumber phoneNumber : phones ) {
            list.add( phoneNumberToPhoneNumberDTO1( phoneNumber ) );
        }

        return list;
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
