package com.innov8ors.backend.dto;

import java.util.List;

import com.innov8ors.backend.model.Phase;

public class RetrieveAllPhasesDto {

    private List<Phase> phases;

    public List<Phase> getPhases() {
        return phases;
    }

    public void setPhases(List<Phase> phases) {
        this.phases = phases;
    }
    
}
