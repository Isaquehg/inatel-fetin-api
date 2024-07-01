package com.innov8ors.backend.dto;

import java.util.List;

public class UpdateTeamMembersDto {
    
    private List<CreateUserDto> members;

    public UpdateTeamMembersDto() {}

    public List<CreateUserDto> getMembers() {
        return members;
    }

    public void setMembers(List<CreateUserDto> members) {
        this.members = members;
    }

}
