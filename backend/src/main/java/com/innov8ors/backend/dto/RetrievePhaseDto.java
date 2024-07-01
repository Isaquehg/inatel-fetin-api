package com.innov8ors.backend.dto;

import java.time.ZonedDateTime;

public class RetrievePhaseDto {

    private String phaseName;
    private ZonedDateTime startAt;
    private ZonedDateTime endAt;

    public RetrievePhaseDto() {}

    public String getPhaseName() {
        return phaseName;
    }
    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }
    public ZonedDateTime getStartAt() {
        return startAt;
    }
    public void setStartAt(ZonedDateTime startAt) {
        this.startAt = startAt;
    }
    public ZonedDateTime getEndAt() {
        return endAt;
    }
    public void setEndAt(ZonedDateTime endAt) {
        this.endAt = endAt;
    }
    
}
