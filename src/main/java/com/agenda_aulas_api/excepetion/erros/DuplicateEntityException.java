package com.agenda_aulas_api.excepetion.erros;

public class DuplicateEntityException extends RuntimeException {

    public DuplicateEntityException(String msg) {
        super(msg);
    }
}
