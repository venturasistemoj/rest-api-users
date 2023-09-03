package com.venturasistemoj.restapi.domain.address;

import com.venturasistemoj.restapi.domain.phone.PhoneNumberDTO;
import com.venturasistemoj.restapi.domain.user.User;
import com.venturasistemoj.restapi.domain.user.UserDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-03T17:42:20-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.2.1 (Amazon.com Inc.)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public Address addressDTOToAddress(AddressDTO addressDTO) {
        if ( addressDTO == null ) {
            return null;
        }

        Address address = new Address();

        address.setUser( userDTOToUser( addressDTO.userDTO() ) );
        address.setAddressId( addressDTO.addressId() );
        address.setPublicPlace( addressDTO.publicPlace() );
        address.setStreetAddress( addressDTO.streetAddress() );
        address.setComplement( addressDTO.complement() );
        address.setCity( addressDTO.city() );
        address.setState( addressDTO.state() );
        address.setZipCode( addressDTO.zipCode() );

        return address;
    }

    @Override
    public AddressDTO addressToAddressDTO(Address address) {
        if ( address == null ) {
            return null;
        }

        UserDTO userDTO = null;
        Long addressId = null;
        String publicPlace = null;
        String streetAddress = null;
        String complement = null;
        String city = null;
        String state = null;
        String zipCode = null;

        userDTO = userToUserDTO( address.getUser() );
        addressId = address.getAddressId();
        publicPlace = address.getPublicPlace();
        streetAddress = address.getStreetAddress();
        complement = address.getComplement();
        city = address.getCity();
        state = address.getState();
        zipCode = address.getZipCode();

        AddressDTO addressDTO = new AddressDTO( addressId, publicPlace, streetAddress, complement, city, state, zipCode, userDTO );

        return addressDTO;
    }

    @Override
    public List<Address> adressesDTOToAdresses(List<AddressDTO> adressesDTO) {
        if ( adressesDTO == null ) {
            return null;
        }

        List<Address> list = new ArrayList<Address>( adressesDTO.size() );
        for ( AddressDTO addressDTO : adressesDTO ) {
            list.add( addressDTOToAddress( addressDTO ) );
        }

        return list;
    }

    @Override
    public List<AddressDTO> adressesToAdressesDTO(List<Address> adresses) {
        if ( adresses == null ) {
            return null;
        }

        List<AddressDTO> list = new ArrayList<AddressDTO>( adresses.size() );
        for ( Address address : adresses ) {
            list.add( addressToAddressDTO1( address ) );
        }

        return list;
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

    protected AddressDTO addressToAddressDTO1(Address address) {
        if ( address == null ) {
            return null;
        }

        Long addressId = null;
        String publicPlace = null;
        String streetAddress = null;
        String complement = null;
        String city = null;
        String state = null;
        String zipCode = null;

        addressId = address.getAddressId();
        publicPlace = address.getPublicPlace();
        streetAddress = address.getStreetAddress();
        complement = address.getComplement();
        city = address.getCity();
        state = address.getState();
        zipCode = address.getZipCode();

        UserDTO userDTO = null;

        AddressDTO addressDTO = new AddressDTO( addressId, publicPlace, streetAddress, complement, city, state, zipCode, userDTO );

        return addressDTO;
    }
}
