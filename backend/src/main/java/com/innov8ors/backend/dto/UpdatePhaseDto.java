package com.innov8ors.backend.dto;

import java.time.ZonedDateTime;

public class UpdatePhaseDto {
    
    private String name;
    private ZonedDateTime start;
    private ZonedDateTime endDate;

    public UpdatePhaseDto() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return endDate;
    }

    public void setEnd(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

}
