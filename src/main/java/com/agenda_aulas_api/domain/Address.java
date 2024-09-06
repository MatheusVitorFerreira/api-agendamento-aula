package com.agenda_aulas_api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private UUID id;

    @NonNull
    @Column(nullable = false)
    private String street;

    @Min(1)
    private int number;

    @NonNull
    @Column(nullable = false)
    private String city;

    @NonNull
    @Column(nullable = false)
    private String state;

    @NonNull
    @Column(nullable = false)
    private String zipCode;

    @NonNull
    @Column(nullable = false)
    private String country;
}
