package com.agenda_aulas_api.excepetion.erros;

public class StudentRepositoryNotFoundException extends RuntimeException {

    public StudentRepositoryNotFoundException(String msg) {
        super(msg);
    }
}
