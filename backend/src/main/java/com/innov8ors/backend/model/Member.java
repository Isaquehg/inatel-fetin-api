package com.innov8ors.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name =  "\"member\"")
public class Member {

    public Member(User user, Team team, boolean responsible_for_team) {
        this.user = user;
        this.team = team;
        this.responsible_for_team = responsible_for_team;
    }

    public Member() {}


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "student_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "responsible_for_team")
    private boolean responsible_for_team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public boolean isResponsible_for_team() {
        return responsible_for_team;
    }

    public void setResponsible_for_team(boolean responsible_for_team) {
        this.responsible_for_team = responsible_for_team;
    }

}
