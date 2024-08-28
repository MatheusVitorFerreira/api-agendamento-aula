package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.Address;
import com.agenda_aulas_api.dto.AddressDTO;
import com.agenda_aulas_api.exception.erros.AddressNotFoundException;
import com.agenda_aulas_api.exception.erros.DatabaseNegatedAccessException;
import com.agenda_aulas_api.exception.erros.InvalidUrlException;

import com.agenda_aulas_api.repository.AddressRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public List<AddressDTO> findAll() {
        try {
            return addressRepository.findAll().stream()
                    .map(AddressDTO::fromAddress)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public AddressDTO findById(UUID id) {
        try {
            Address address = addressRepository.findById(id)
                    .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + id));
            return AddressDTO.fromAddress(address);
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public Page<AddressDTO> findPageAddress(Integer page, Integer linesPerPage, String orderBy, String direction) {
        try {
            PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
            return addressRepository.findAll(pageRequest).map(AddressDTO::fromAddress);
        } catch (IllegalArgumentException e) {
            throw new InvalidUrlException("Invalid URL or sorting parameter: " + e.getMessage());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    @Transactional
    public AddressDTO updateAddress(AddressDTO objDto, UUID idAddress) {
        try {
            Address existingAddress = addressRepository.findById(idAddress)
                    .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + idAddress));
            existingAddress.setCity(objDto.getCity());
            existingAddress.setCountry(objDto.getCountry());
            existingAddress.setState(objDto.getState());
            existingAddress.setZipCode((objDto.getZipCode()));
            existingAddress.setNumber(objDto.getNumber());
            existingAddress.setStreet(objDto.getStreet());
            Address updateAddress = addressRepository.save(existingAddress);
            return AddressDTO.fromAddress(updateAddress);
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to update address in the database: " + e.getMessage());
        }
    }

    public void deleteAddress(UUID idAddress) {
        Address existingAddress = addressRepository.findById(idAddress)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + idAddress));
        addressRepository.deleteById(idAddress);
    }
}
