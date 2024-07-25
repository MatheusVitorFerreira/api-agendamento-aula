package com.agenda_aulas_api.excepetion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardError implements Serializable {

    private Integer status;
    private String msg;
    private Long timeStamp;

}
