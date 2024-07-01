package com.innov8ors.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.innov8ors.backend.model.Member;
import com.innov8ors.backend.model.User;

public interface MemberRepository extends CrudRepository<Member, Long> {
    List<Member> findByTeamId(Long teamId);
    List<Member> findByUser(User user);
    Optional<Member> findByUserAndTeamId(User user, Long teamId);
}
