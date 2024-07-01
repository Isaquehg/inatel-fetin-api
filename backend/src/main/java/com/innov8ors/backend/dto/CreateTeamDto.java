package com.innov8ors.backend.dto;

import java.util.List;

public class CreateTeamDto {
    private String teamName;
    private List<CreateUserDto> members;
    private CreateUserDto advisor;

    public CreateTeamDto() {}

    public CreateTeamDto(String teamName, List<CreateUserDto> members, CreateUserDto advisor) {
        this.teamName = teamName;
        this.members = members;
        this.advisor = advisor;
    }

    // Getters and setters
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<CreateUserDto> getMembers() {
        return members;
    }

    public void setMembers(List<CreateUserDto> members) {
        this.members = members;
    }

    public CreateUserDto getAdvisor() {
        return advisor;
    }

    public void setAdvisor(CreateUserDto advisor) {
        this.advisor = advisor;
    }

}
