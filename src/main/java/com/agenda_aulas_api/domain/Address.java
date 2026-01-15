package com.agenda_aulas_api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "tb_addresses")
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID addressId;

    private String street;
    private Integer number;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    @OneToOne
    @JoinColumn(name = "student_id", unique = true)
    private Student student;

    @OneToOne
    @JoinColumn(name = "teacher_id", unique = true)
    private Teacher teacher;
}
