
package com.innov8ors.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"grade\"")
public class Grade {

    public Grade(int grade){
        this.grade = grade;
    }

    public Grade(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grade", nullable = false)
    private int grade;

    @ManyToOne()
    @JoinColumn(name = "phase_id")
    private Phase phase;

    @ManyToOne()
    @JoinColumn(name = "team_id")
    private Team team;


    public Long getTeam_id() {
        return this.team.getId();
    }
    
    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPhase_id(){
        return this.phase.getId();
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }
}
