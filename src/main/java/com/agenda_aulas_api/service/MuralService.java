package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.MuralComment;
import com.agenda_aulas_api.domain.MuralPost;
import com.agenda_aulas_api.domain.User;
import com.agenda_aulas_api.dto.record.*;
import com.agenda_aulas_api.repository.MuralCommentRepository;
import com.agenda_aulas_api.repository.MuralPostRepository;
import com.agenda_aulas_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MuralService {

    private final MuralPostRepository postRepository;
    private final MuralCommentRepository commentRepository;
    private final UserRepository userRepository;


    public MuralPostResponseRecordDTO createPost(MuralPostRequestRecordDTO dto, Jwt jwt) {
        User author = getUserFromJwt(jwt);

        MuralPost post = MuralPost.builder()
                .title(dto.title())
                .content(dto.content())
                .parentId(dto.parentId())
                .parentType(dto.parentType()) // Ex: "lesson" ou "classroom"
                .author(author)
                .build();

        postRepository.save(post);

        return MuralPostResponseRecordDTO.fromEntity(post);
    }

    public MuralCommentResponseRecordDTO createComment(MuralCommentRequestRecordDTO dto, Jwt jwt) {
        User author = getUserFromJwt(jwt);

        MuralPost post = postRepository.findById(dto.postId())
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        MuralComment comment = MuralComment.builder()
                .text(dto.text())
                .author(author)
                .post(post)
                .build();

        commentRepository.save(comment);

        return MuralCommentResponseRecordDTO.fromEntity(comment);
    }


    public List<MuralPostResponseRecordDTO> listPosts() {
        return postRepository.findAll().stream()
                .map(MuralPostResponseRecordDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private User getUserFromJwt(Jwt jwt) {
        String idClaim = jwt.getClaimAsString("userId");
        if (idClaim == null) {
            throw new RuntimeException("JWT não contém o claim 'userId'");
        }

        UUID userId = UUID.fromString(idClaim);
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}
