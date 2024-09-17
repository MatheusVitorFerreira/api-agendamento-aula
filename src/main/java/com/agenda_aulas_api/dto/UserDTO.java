package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.Role;
import com.agenda_aulas_api.domain.User;
import com.agenda_aulas_api.domain.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private UUID userId;
    private String username;
    private UserType userType;
    private List<Role> roles;
    private UUID professorId;
    private UUID alunoId;
    private LocalDateTime createdAt;

    public User toUser() {
        User user = new User();
        user.setUserID(this.userId);
        user.setUsername(this.username);
        user.setUserType(this.userType);
        user.setRoles(this.roles);
        user.setProfessorId(this.professorId);
        user.setAlunoId(this.alunoId);
        user.setCreatedAt(this.createdAt);
        return user;
    }

    public static UserDTO fromUser(User user) {
        return new UserDTO(
                user.getUserID(),
                user.getUsername(),
                user.getUserType(),
                user.getRoles(),
                user.getProfessorId(),
                user.getAlunoId(),
                user.getCreatedAt()
        );
    }
}
