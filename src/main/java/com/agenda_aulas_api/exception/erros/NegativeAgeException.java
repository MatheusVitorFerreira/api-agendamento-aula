package com.agenda_aulas_api.exception.erros;

public class NegativeAgeException extends RuntimeException {
    public NegativeAgeException(String msg) {
        super(msg);
    }
}
