package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {

    boolean existsByStreetAndNumberAndCityAndStateAndZipCodeAndCountry(
            String street, Integer number, String city, String state, String zipCode, String country);
}
