package com.agenda_aulas_api.domain;

import com.agenda_aulas_api.exception.erros.NegativeAgeException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
public abstract class Person {

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private int age;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(nullable = false, unique = true)
    @CPF
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    private String telephone;

    public void setAge(int age) {
        if (age <= 0) {
            throw new NegativeAgeException("Age must be a positive number.");
        }
        this.age = age;
    }

    public void setBirthDate(LocalDate birthDate) {
        if (birthDate == null || birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid date of birth.");
        }
        this.birthDate = birthDate;
    }
}
