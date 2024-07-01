package com.innov8ors.backend.dto;

public class RetrieveGradeDto {

    private String phaseName;
    private int grade;

    public RetrieveGradeDto() {}

    public String getPhaseName() {
        return phaseName;
    }
    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }
    public int getGrade() {
        return grade;
    }
    public void setGrade(int grade) {
        this.grade = grade;
    }
    
}
