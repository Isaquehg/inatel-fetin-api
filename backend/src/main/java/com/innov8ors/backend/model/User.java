package com.innov8ors.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.apache.commons.validator.routines.EmailValidator;


@Entity
@Table(name = "\"user\"")
public class User{

    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roleString = role;
    }

    // Constructor for Hibernate
    public User(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;*/

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String roleString;
    

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(emailValidator(email)){
            this.email = email;
        }
        else{
            throw new IllegalArgumentException("Invalid email");
        }

    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    private boolean emailValidator(String email){
        return EmailValidator.getInstance().isValid(email);
    }

    public String getRole() {
        return roleString;
    }

    public void setRole(String role) {
        this.roleString = role;
    }

}
