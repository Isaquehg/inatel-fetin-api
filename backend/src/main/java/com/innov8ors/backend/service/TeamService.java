package com.innov8ors.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innov8ors.backend.model.Member;
import com.innov8ors.backend.repository.MemberRepository;

@Service
public class TeamService {
    
    @Autowired
    private MemberRepository memberRepository;

    public List<Member> getMembersByTeamId(Long teamId) {
        return memberRepository.findByTeamId(teamId);
    }
}
