package com.agenda_aulas_api.excepetion.erros;

public class NegativeAgeException extends RuntimeException {
    public NegativeAgeException(String msg) {
        super(msg);
    }
}
