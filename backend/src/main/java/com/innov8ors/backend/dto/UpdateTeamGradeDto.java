package com.innov8ors.backend.dto;

public class UpdateTeamGradeDto {
    
    private Long phaseId;
    private int grade;

    public UpdateTeamGradeDto() {}

    public Long getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Long phaseId) {
        this.phaseId = phaseId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

}
