package com.agenda_aulas_api.exception.erros;

public class ScheduleClassRepositoryNotFoundException extends RuntimeException {

    public ScheduleClassRepositoryNotFoundException(String msg) {
        super(msg);
    }
}
