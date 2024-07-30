package com.agenda_aulas_api.domain;

import com.agenda_aulas_api.excepetion.erros.NegativeAgeException;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;
import java.time.LocalDate;

@MappedSuperclass
@Data
@NoArgsConstructor
public abstract class Person implements Serializable {

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private int age;

    @Column(name = "birth_date")
    private LocalDate birthDateTime;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(nullable = false, unique = true)
    @CPF
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String telephone;

    public void setAge(int age) {
        if (age <= 0) {
            throw new NegativeAgeException("Age must be a positive number.");
        }
        this.age = age;
    }

    public void setPositiveDateOfBirth(LocalDate birthDateTime) {
        if (birthDateTime == null || birthDateTime.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid date of birth.");
        }
        this.birthDateTime = birthDateTime;
    }
}
