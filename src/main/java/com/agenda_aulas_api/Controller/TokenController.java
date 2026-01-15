package com.agenda_aulas_api.Controller;


import com.agenda_aulas_api.dto.record.LoginRequestDTO;
import com.agenda_aulas_api.dto.record.LoginResponseDTO;
import com.agenda_aulas_api.domain.User;
import com.agenda_aulas_api.repository.UserRepository;
import com.agenda_aulas_api.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sistema-agendamento-aula/api/v1/token")
public class TokenController {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new BadCredentialsException("Usuário ou senha inválidos"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }

        String token = tokenService.generateToken(user).getToken();
        return ResponseEntity.ok(new LoginResponseDTO(token, 3600L));
    }
}
