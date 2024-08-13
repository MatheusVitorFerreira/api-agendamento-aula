package com.agenda_aulas_api.exception.erros;

public class NoAvailableSlotsException extends RuntimeException {

    public NoAvailableSlotsException(String msg) {
        super(msg);
    }
}
