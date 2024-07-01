package com.innov8ors.backend.dto;

import com.innov8ors.backend.model.User;

public class RecoveryTokenDto {
    private String token;
    private User user; // Debug purposes

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
