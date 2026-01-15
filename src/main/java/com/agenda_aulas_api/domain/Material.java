package com.agenda_aulas_api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_material")
public class Material implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType;

    @Lob
    @Column(nullable = false)
    private byte[] data;

    @Column(nullable = false)
    private UUID parentId;

    @Column(nullable = false)
    private String parentType;

    @Builder.Default
    private LocalDateTime uploadDate = LocalDateTime.now();
}
