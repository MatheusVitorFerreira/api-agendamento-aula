package com.agenda_aulas_api.exception.erros;

public class DatabaseNegatedAccessException extends RuntimeException {

    public DatabaseNegatedAccessException(String msg) {
        super(msg);
    }
}
