package com.agenda_aulas_api.exception.erros;

public class TeacherNotFoundException extends RuntimeException {

    public TeacherNotFoundException(String msg) {
        super(msg);
    }
}
