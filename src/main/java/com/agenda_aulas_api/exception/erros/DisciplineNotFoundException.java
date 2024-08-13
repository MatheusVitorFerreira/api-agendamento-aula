package com.agenda_aulas_api.exception.erros;

public class DisciplineNotFoundException extends RuntimeException {

    public DisciplineNotFoundException(String msg) {
        super(msg);
    }
}
