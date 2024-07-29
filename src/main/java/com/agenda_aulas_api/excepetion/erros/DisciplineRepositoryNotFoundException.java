package com.agenda_aulas_api.excepetion.erros;

public class DisciplineRepositoryNotFoundException extends RuntimeException {

    public DisciplineRepositoryNotFoundException(String msg) {
        super(msg);
    }
}
