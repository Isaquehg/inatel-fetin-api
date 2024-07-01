package com.innov8ors.backend.dto;

public class RetrieveMemberDto {
    private Long id;
    private String name;
    private String email;
    
    public RetrieveMemberDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    RetrieveMemberDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        this.email = email;
    }

    // Getters and setters
    
}
