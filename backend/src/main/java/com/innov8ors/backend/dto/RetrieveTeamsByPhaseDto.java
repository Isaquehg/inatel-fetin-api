package com.innov8ors.backend.dto;

import java.util.List;

public class RetrieveTeamsByPhaseDto {

    private List<RetrieveProjectDetailsDto> teams;

    public RetrieveTeamsByPhaseDto() {}

    public List<RetrieveProjectDetailsDto> getTeams() {
        return teams;
    }

    public void setTeams(List<RetrieveProjectDetailsDto> teams) {
        this.teams = teams;
    }
    
}
