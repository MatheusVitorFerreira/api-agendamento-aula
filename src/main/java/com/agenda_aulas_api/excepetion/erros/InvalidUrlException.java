package com.agenda_aulas_api.excepetion.erros;

public class InvalidUrlException extends RuntimeException {
    public InvalidUrlException(String msg) {
        super(msg);
    }
}
