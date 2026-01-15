package com.agenda_aulas_api.exception.erros;

public class InvalidScheduleException extends RuntimeException {

    public InvalidScheduleException(String msg) {
        super(msg);
    }
}
