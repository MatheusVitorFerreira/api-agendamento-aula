package com.agenda_aulas_api.exception.erros;

public class InvalidUrlException extends RuntimeException {
    public InvalidUrlException(String msg) {
        super(msg);
    }
}
