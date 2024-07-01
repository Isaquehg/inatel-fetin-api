package com.innov8ors.backend.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.innov8ors.backend.model.Phase;

public interface PhaseRepository extends CrudRepository<Phase, Long> {
        List<Phase> findByStartBeforeAndEndDateAfter(ZonedDateTime start, ZonedDateTime endDate);
}