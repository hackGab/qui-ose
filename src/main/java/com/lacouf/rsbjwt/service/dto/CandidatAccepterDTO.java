package com.lacouf.rsbjwt.service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidatAccepterDTO {

    private Long id;
    private Long entrevueId;
    private boolean accepte;

    public CandidatAccepterDTO(Long entrevueId, boolean accepte) {
        this.entrevueId = entrevueId;
        this.accepte = accepte;
    }
}
