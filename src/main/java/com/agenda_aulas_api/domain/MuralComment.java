package com.agenda_aulas_api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "mural_Comment")
public class MuralComment implements Serializable {


        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private UUID id;

        @Column(columnDefinition = "TEXT", nullable = false)
        private String text;

        private LocalDateTime createdAt = LocalDateTime.now();

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "post_id", nullable = false)
        @JsonIgnoreProperties({"comments", "lesson", "author"})
        private MuralPost post;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "author_id", nullable = false)
        @JsonIgnoreProperties({"password", "roles"})
        private User author;
    }