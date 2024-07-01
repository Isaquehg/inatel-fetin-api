package com.innov8ors.backend.dto;

import java.util.List;
public class RetrieveTeamsDto {
    
    private RetrievePhaseDto fetinPhase;
    private List<RetrieveTeamDto> teams;

    public RetrieveTeamsDto(RetrievePhaseDto fetinPhase, List<RetrieveTeamDto> teams) {
        this.fetinPhase = fetinPhase;
        this.teams = teams;
    }

    public RetrieveTeamsDto() {}

    public RetrievePhaseDto getFetinPhase() {
        return fetinPhase;
    }

    public void setFetinPhase(RetrievePhaseDto fetinPhase) {
        this.fetinPhase = fetinPhase;
    }

    public List<RetrieveTeamDto> getTeams() {
        return teams;
    }

    public void setTeams(List<RetrieveTeamDto> teams) {
        this.teams = teams;
    }

}
