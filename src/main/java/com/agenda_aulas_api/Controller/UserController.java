package com.agenda_aulas_api.Controller;


import com.agenda_aulas_api.domain.User;
import com.agenda_aulas_api.dto.CreateUserDTO;
import com.agenda_aulas_api.dto.UserResponseDTO;
import com.agenda_aulas_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/sistema-agendamento-aula/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {

        List<UserResponseDTO> users = userService.findAll();

        return ResponseEntity.ok(users);
    }
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateUserDTO dto) {

        User user = userService.create(dto);

        URI uri = URI.create("/api/v1/users/" + user.getUserId());
        return ResponseEntity.created(uri).build();
    }
}
