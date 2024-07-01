package com.innov8ors.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;


@Entity
@Table(name = "\"team\"")
public class Team {
    
    public Team(String name, Boolean active, User advisor ){
        this.name = name;
        this.active = active;
        this.advisor = advisor;
    }

    public Team(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_name", nullable = false)
    private String name;

    @OneToOne()
    @JoinColumn(name = "advisor_id")
    private User advisor;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @OneToMany(mappedBy = "team")
    private List<Member> member;

    //Getters and setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getAdvisor(){
        return advisor;
    }

    public void setAdvisor(User advisor){
         this.advisor = advisor;
    }
    
    public Boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean emailValidator(String email){
        return EmailValidator.getInstance().isValid(email);
    }
}