package com.agenda_aulas_api.Controller;

import com.agenda_aulas_api.dto.AddressDTO;
import com.agenda_aulas_api.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sistema-agendamento-aula/api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    @Autowired
    private final AddressService addressService;

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_MODERATOR') or hasAuthority('SCOPE_BASIC')")

    public ResponseEntity<List<AddressDTO>> getAllAddress() {
        List<AddressDTO> addressDTOList = addressService.findAll();
        return ResponseEntity.ok(addressDTOList);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_MODERATOR') or hasAuthority('SCOPE_BASIC')")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable UUID id) {
        AddressDTO addressDTO = addressService.findById(id);
        return ResponseEntity.ok(addressDTO);
    }

    @GetMapping(value = "/page")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_MODERATOR') or hasAuthority('SCOPE_BASIC')")
    public ResponseEntity<Page<AddressDTO>> findPageAddress(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        Page<AddressDTO> addressDTOPage = addressService.findPageAddress(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(addressDTOPage);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<AddressDTO> update(@RequestBody AddressDTO objDto, @PathVariable UUID id) {
        AddressDTO newObj = addressService.updateAddress(objDto, id);
        return ResponseEntity.ok().body(newObj);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
