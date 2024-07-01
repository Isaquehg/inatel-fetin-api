package com.innov8ors.backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.innov8ors.backend.model.Team;

public interface TeamRepository extends CrudRepository<Team, Long> {
    List<Team> findByAdvisorId(Long advisorId);
    List<Team> findByActive(boolean isActive);
}
