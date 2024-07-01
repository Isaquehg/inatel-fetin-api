package com.innov8ors.backend.dto;

import java.util.List;

public class RetrieveProjectDetailsDto {
    private Long teamId;
    private String teamName;
    private Boolean active;
    private Long advisorId;
    private List<RetrieveMemberDto> members;
    private List<RetrieveGradeDto> grades;

    public List<RetrieveGradeDto> getGrades() {
        return grades;
    }

    public void setGrades(List<RetrieveGradeDto> grades) {
        this.grades = grades;
    }

    public RetrieveProjectDetailsDto() {}

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getAdvisorId() {
        return advisorId;
    }

    public void setAdvisorId(Long advisorId) {
        this.advisorId = advisorId;
    }

    public List<RetrieveMemberDto> getMembers() {
        return members;
    }

    public void setMembers(List<RetrieveMemberDto> members) {
        this.members = members;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

}
