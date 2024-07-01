package com.innov8ors.backend.dto;

public class CreateUserDto {
    private String name;
    private String email;
    //private String password;
    //private String role;

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    /*public String getPassword() {
        return password;
    }
    public String getRole() {
        return role;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRole(String role) {
        this.role = role;
    }
    */

}
