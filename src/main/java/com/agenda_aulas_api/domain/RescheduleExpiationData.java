package com.agenda_aulas_api.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RescheduleExpiationData  implements Serializable {

    private LocalDateTime requestDate;
    private LocalDateTime expiryDate;
    private String reason;
    private boolean isApproved;

    public boolean isValid() {
        if (expiryDate == null) {
            return false;
        }
        return !LocalDateTime.now().isAfter(expiryDate);
    }
}
