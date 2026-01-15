package com.agenda_aulas_api.Controller;


import com.agenda_aulas_api.dto.record.MuralPostRequestRecordDTO;
import com.agenda_aulas_api.dto.record.MuralPostResponseRecordDTO;
import com.agenda_aulas_api.dto.record.MuralCommentRequestRecordDTO;
import com.agenda_aulas_api.dto.record.MuralCommentResponseRecordDTO;
import com.agenda_aulas_api.service.MuralService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sistema-agendamento-aula/api/v1/mural")
@RequiredArgsConstructor
public class MuralController {

    private final MuralService muralService;


    @PostMapping("/post")
    public ResponseEntity<MuralPostResponseRecordDTO> createPost(
            @RequestBody MuralPostRequestRecordDTO dto,
            @RequestAttribute("jwt") Jwt jwt
    ) {
        var post = muralService.createPost(dto, jwt);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/comment")
    public ResponseEntity<MuralCommentResponseRecordDTO> createComment(
            @RequestBody MuralCommentRequestRecordDTO dto,
            @RequestAttribute("jwt") Jwt jwt
    ) {
        var comment = muralService.createComment(dto, jwt);
        return ResponseEntity.ok(comment);
    }


    @GetMapping("/posts")
    public ResponseEntity<List<MuralPostResponseRecordDTO>> listPosts() {
        var posts = muralService.listPosts();
        return ResponseEntity.ok(posts);
    }
}
