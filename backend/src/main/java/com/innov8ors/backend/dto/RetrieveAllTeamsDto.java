package com.innov8ors.backend.dto;

import java.util.Map;

public class RetrieveAllTeamsDto {

    private Map<Long, String> teams;

    public RetrieveAllTeamsDto() {}

    public Map<Long, String> getTeams() {
        return teams;
    }

    public void setTeams(Map<Long, String> teams) {
        this.teams = teams;
    }
    
}
