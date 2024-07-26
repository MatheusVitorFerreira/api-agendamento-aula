package com.agenda_aulas_api.excepetion.erros;

public class DatabaseNegatedAccessException extends RuntimeException {

    public DatabaseNegatedAccessException(String msg) {
        super(msg);
    }
}
