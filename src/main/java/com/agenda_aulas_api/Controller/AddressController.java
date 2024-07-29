package com.agenda_aulas_api.Controller;

import com.agenda_aulas_api.dto.AddressDTO;
import com.agenda_aulas_api.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sistema-agendamento-aula/api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    @Autowired
    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAdress() {
        List<AddressDTO> addressDTOList = addressService.findAll();
        return ResponseEntity.ok(addressDTOList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable UUID id) {
        AddressDTO addressDTO = addressService.findById(id);
        return ResponseEntity.ok(addressDTO);
    }

    @GetMapping(value = "/page")
    public ResponseEntity<Page<AddressDTO>> findPageEspecialidade(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        Page<AddressDTO> addressDTOPage = addressService.findPageAddress(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(addressDTOPage);
    }

    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO savedAddresDTO = addressService.createAddress(addressDTO);
        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedAddresDTO.getId())
                .toUri();
        return ResponseEntity.created(headerLocation).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AddressDTO> update(@RequestBody AddressDTO objDto, @PathVariable UUID id) {
        AddressDTO newObj = addressService.updateAddress(objDto, id);
        return ResponseEntity.ok().body(newObj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
