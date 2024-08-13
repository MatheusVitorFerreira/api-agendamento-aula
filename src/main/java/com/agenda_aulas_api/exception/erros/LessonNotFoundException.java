package com.agenda_aulas_api.exception.erros;

public class LessonNotFoundException extends RuntimeException {

    public LessonNotFoundException(String msg) {
        super(msg);
    }
}
