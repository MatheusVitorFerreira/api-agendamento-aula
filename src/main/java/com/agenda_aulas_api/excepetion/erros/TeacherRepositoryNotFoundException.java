package com.agenda_aulas_api.excepetion.erros;

public class TeacherRepositoryNotFoundException extends RuntimeException {

    public TeacherRepositoryNotFoundException(String msg) {
        super(msg);
    }
}
