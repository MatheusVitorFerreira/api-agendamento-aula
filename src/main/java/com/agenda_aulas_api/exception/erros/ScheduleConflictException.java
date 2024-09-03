package com.agenda_aulas_api.exception.erros;

public class ScheduleConflictException extends RuntimeException {
    public ScheduleConflictException(String msg) {
        super(msg);
    }
}
