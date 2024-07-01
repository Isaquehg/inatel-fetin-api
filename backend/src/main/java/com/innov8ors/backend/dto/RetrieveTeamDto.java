package com.innov8ors.backend.dto;

public class RetrieveTeamDto {
    private Long id;
    private String name;
    private Boolean active;
    private RetrieveAdvisorDto advisor;

    public RetrieveTeamDto(Long id, String name, Boolean active, RetrieveAdvisorDto advisor) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.advisor = advisor;
    }

    public RetrieveTeamDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public RetrieveAdvisorDto getAdvisor() {
        return advisor;
    }

    public void setAdvisor(RetrieveAdvisorDto advisor) {
        this.advisor = advisor;
    }

    
}
