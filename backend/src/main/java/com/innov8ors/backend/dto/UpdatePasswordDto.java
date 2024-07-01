package com.innov8ors.backend.dto;

public class UpdatePasswordDto {
    
    private String newPassword;

    public UpdatePasswordDto() {}

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
