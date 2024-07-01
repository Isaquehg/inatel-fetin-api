package com.innov8ors.backend.dto;

public class UpdateTeamAdvisorDto {
    
    private CreateUserDto newAdvisor;

    public UpdateTeamAdvisorDto() {}

    public CreateUserDto getNewAdvisor() {
        return newAdvisor;
    }

    public void setNewAdvisor(CreateUserDto newAdvisor) {
        this.newAdvisor = newAdvisor;
    }

}
