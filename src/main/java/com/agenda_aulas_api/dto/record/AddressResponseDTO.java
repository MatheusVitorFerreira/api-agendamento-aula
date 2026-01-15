package com.agenda_aulas_api.dto.record;

import com.agenda_aulas_api.domain.Address;

public record AddressResponseDTO(
        String street,
        Integer number,
        String city,
        String state,
        String zipCode,
        String country
) {
    public static AddressResponseDTO fromAddress(Address a) {
        return new AddressResponseDTO(
                a.getStreet(),
                a.getNumber(),
                a.getCity(),
                a.getState(),
                a.getZipCode(),
                a.getCountry()
        );
    }
}
