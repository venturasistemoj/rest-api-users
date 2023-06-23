package com.venturasistemoj.restapi.domain.address;

import com.venturasistemoj.restapi.domain.user.User;
import com.venturasistemoj.restapi.domain.user.UserDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-23T12:06:29-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.33.0.v20230218-1114, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public Address addressDTOToAddress(AddressDTO addressDTO) {
        if ( addressDTO == null ) {
            return null;
        }

        Address address = new Address();

        address.setUser( userDTOToUser( addressDTO.getUserDTO() ) );
        address.setAddressId( addressDTO.getAddressId() );
        address.setCity( addressDTO.getCity() );
        address.setComplement( addressDTO.getComplement() );
        address.setPublicPlace( addressDTO.getPublicPlace() );
        address.setState( addressDTO.getState() );
        address.setStreetAddress( addressDTO.getStreetAddress() );
        address.setZipCode( addressDTO.getZipCode() );

        return address;
    }

    @Override
    public AddressDTO addressToAddressDTO(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDTO.AddressDTOBuilder addressDTO = AddressDTO.builder();

        addressDTO.userDTO( userToUserDTO( address.getUser() ) );
        addressDTO.addressId( address.getAddressId() );
        addressDTO.city( address.getCity() );
        addressDTO.complement( address.getComplement() );
        addressDTO.publicPlace( address.getPublicPlace() );
        addressDTO.state( address.getState() );
        addressDTO.streetAddress( address.getStreetAddress() );
        addressDTO.zipCode( address.getZipCode() );

        return addressDTO.build();
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

    protected AddressDTO addressToAddressDTO1(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDTO.AddressDTOBuilder addressDTO = AddressDTO.builder();

        addressDTO.addressId( address.getAddressId() );
        addressDTO.city( address.getCity() );
        addressDTO.complement( address.getComplement() );
        addressDTO.publicPlace( address.getPublicPlace() );
        addressDTO.state( address.getState() );
        addressDTO.streetAddress( address.getStreetAddress() );
        addressDTO.zipCode( address.getZipCode() );

        return addressDTO.build();
    }
}
