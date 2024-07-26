package com.agenda_aulas_api.excepetion.erros;

public class AddressRepositoryNotFoundException extends RuntimeException {

    public AddressRepositoryNotFoundException(String msg) {
        super(msg);
    }
}
