package com.agenda_aulas_api.Controller;

import com.agenda_aulas_api.dto.CreateUserDTO;
import com.agenda_aulas_api.dto.UserDTO;
import com.agenda_aulas_api.exception.erros.UserCreationException;
import com.agenda_aulas_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/sistema-agendamento-aula/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_MODERATOR') or hasAuthority('SCOPE_BASIC')")
    public ResponseEntity<Object> newUser(
            @PathVariable("id") String id,
            @RequestBody CreateUserDTO dto) {
        try {
            userService.createUser(id, dto);
            return ResponseEntity.ok().build();
        } catch (UserCreationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_MODERATOR')")
    public ResponseEntity<List<UserDTO>> listUsers() {
        var users = userService.findAll();
        return ResponseEntity.ok(users);
    }
}

