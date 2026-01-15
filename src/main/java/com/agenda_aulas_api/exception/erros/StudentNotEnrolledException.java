package com.agenda_aulas_api.exception.erros;

public class StudentNotEnrolledException extends RuntimeException {

    public StudentNotEnrolledException(String msg) {
        super(msg);
    }
}
