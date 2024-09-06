package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private String street;
    private Integer number;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    public Address toAddress() {
        Address address = new Address();
        address.setStreet(this.street);
        address.setNumber(this.number);
        address.setCity(this.city);
        address.setState(this.state);
        address.setZipCode(this.zipCode);
        address.setCountry(this.country);
        return address;
    }

    public static AddressDTO fromAddress(Address address) {
        return new AddressDTO(
                address.getStreet(),
                address.getNumber(),
                address.getCity(),
                address.getState(),
                address.getZipCode(),
                address.getCountry()
        );
    }
}
