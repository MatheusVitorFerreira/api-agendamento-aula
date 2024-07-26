package com.agenda_aulas_api.Controller;

import com.agenda_aulas_api.dto.AddressDTO;
import com.agenda_aulas_api.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable UUID id) {
        AddressDTO addressDTO = addressService.findById(id);
        return ResponseEntity.ok(addressDTO);
    }

    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO savedAddresDTO = addressService.insert(addressDTO);
        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedAddresDTO.getId())
                .toUri();
        return ResponseEntity.created(headerLocation).build();
    }
}
