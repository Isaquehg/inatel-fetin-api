package com.innov8ors.backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.innov8ors.backend.model.Grade;

public interface GradeRepository extends CrudRepository<Grade, Long> {
    List<Grade> findGradeByTeamId(Long teamId);
    List<Grade> findGradeByPhaseId(Long gradeId);
}
