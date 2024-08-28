package com.agenda_aulas_api.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClassShift {

    MANHA(1, "MANHÃ"),
    TARDE(2, "TARDE"),
    NOITE(3, "NOITE");

    private final int cod;
    private final String description;

    public static StatusClass toEnum(Integer cod) {
        if (cod == null) {
            return null;
        }
        for (StatusClass x : StatusClass.values()) {
            if (cod.equals(x.getCod())) {
                return x;
            }
        }
        throw new IllegalArgumentException("Invalid ID: " + cod);
    }
}
