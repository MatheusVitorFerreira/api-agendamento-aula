package com.agenda_aulas_api.exception.erros;

public class AddressNotFoundException extends RuntimeException {

    public AddressNotFoundException(String msg) {
        super(msg);
    }
}
