package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.Discipline;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisciplineDTO {

    private UUID idDiscipline;
    private String name;

    public Discipline toDiscipline() {
        Discipline discipline = new Discipline();
        discipline.setIdDiscipline(this.idDiscipline);
        discipline.setName(this.name);
        return discipline;
    }

    public static DisciplineDTO fromDiscipline(Discipline discipline) {
        return new DisciplineDTO(
                discipline.getIdDiscipline(),
                discipline.getName()
        );
    }
}
