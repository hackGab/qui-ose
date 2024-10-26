package com.lacouf.rsbjwt.service.dto;

import com.lacouf.rsbjwt.model.CandidatAccepter;
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

    public CandidatAccepterDTO(CandidatAccepter candidatAccepter) {
        this.id = candidatAccepter.getId();
        this.entrevueId = candidatAccepter.getEntrevue().getId();
        this.accepte = candidatAccepter.isAccepte();
    }

    @Override
    public String toString() {
        return "CandidatAccepterDTO{" +
                "entrevueId=" + entrevueId +
                ", accepte=" + accepte +
                '}';
    }
}
