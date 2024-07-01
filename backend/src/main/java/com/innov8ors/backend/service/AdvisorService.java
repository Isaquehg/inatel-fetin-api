package com.innov8ors.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import com.innov8ors.backend.dto.RetrieveAdvisorDto;
import com.innov8ors.backend.dto.RetrieveGradeDto;
import com.innov8ors.backend.dto.RetrieveMemberDto;
import com.innov8ors.backend.dto.RetrievePhaseDto;
import com.innov8ors.backend.dto.RetrieveTeamsDto;
import com.innov8ors.backend.dto.RetrieveProjectDetailsDto;
import com.innov8ors.backend.dto.RetrieveTeamDto;
import com.innov8ors.backend.model.Grade;
import com.innov8ors.backend.model.Phase;
import com.innov8ors.backend.model.Team;
import com.innov8ors.backend.model.User;
import com.innov8ors.backend.repository.GradeRepository;
import com.innov8ors.backend.repository.MemberRepository;
import com.innov8ors.backend.repository.PhaseRepository;
import com.innov8ors.backend.repository.TeamRepository;
import com.innov8ors.backend.repository.UserRepository;
import com.innov8ors.backend.security.config.JwtTokenProvider;

@Service
public class AdvisorService {

    @Autowired
    PhaseService phaseService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PhaseRepository phaseRepository;

    @Autowired
    GradeRepository gradeRepository;

    public RetrieveTeamsDto findTutoredProjects(String authorizationHeader) throws AuthenticationCredentialsNotFoundException {
        try {
            // Get email from JWT
            String jwtToken = authorizationHeader.substring(7); // Remove "Bearer "
            String email = JwtTokenProvider.getEmailFromToken(jwtToken);
            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                Long advisorId = user.getId();

                // Retrieve Oriented Teams
                List<Team> teams = teamRepository.findByAdvisorId(advisorId);

                // Map Advisor to DTO
                List<RetrieveTeamDto> teamDtos = teams.stream().map(team -> {
                    RetrieveAdvisorDto advisorDto = new RetrieveAdvisorDto(
                        team.getAdvisor().getId(),
                        team.getAdvisor().getName(),
                        team.getAdvisor().getEmail()
                    );
                    return new RetrieveTeamDto(
                        team.getId(),
                        team.getName(),
                        team.getActive(),
                        advisorDto
                    );
                }).collect(Collectors.toList());

                // Current Phase
                Phase currentPhase = phaseService.getCurrentPhase();

                // Phase DTO
                RetrievePhaseDto phaseDto = new RetrievePhaseDto();
                phaseDto.setPhaseName(currentPhase.getName());
                phaseDto.setStartAt(currentPhase.getStart());
                phaseDto.setEndAt(currentPhase.getEndDate());

                // // Response DTO
                RetrieveTeamsDto dto = new RetrieveTeamsDto();
                dto.setFetinPhase(phaseDto);
                dto.setTeams(teamDtos);
                return dto;
            } else {
                throw new AuthenticationCredentialsNotFoundException("Usuário não encontrado para o email fornecido.");
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw new AuthenticationCredentialsNotFoundException("Token JWT inválido.");
        } catch (AuthenticationCredentialsNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Ocorreu um erro ao recuperar os projetos orientados.", e);
        }
    }

    public RetrieveProjectDetailsDto findTutoredProjectDetails(String authorizationHeader, Long teamId) throws AuthenticationCredentialsNotFoundException {
        try {
            // Get email from JWT
            String jwtToken = authorizationHeader.substring(7); // Remove "Bearer "
            String email = JwtTokenProvider.getEmailFromToken(jwtToken);
            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                Long advisorId = user.getId();

                // Get Team
                Optional<Team> teamOptional = teamRepository.findById(teamId);

                if (teamOptional.isPresent()) {
                    Team team = teamOptional.get();

                    // Map members to DTO
                    List<RetrieveMemberDto> memberDtos = memberRepository.findByTeamId(teamId).stream()
                        .map(member -> new RetrieveMemberDto(
                            member.getUser().getId(),
                            member.getUser().getName(),
                            member.getUser().getEmail()
                        )).collect(Collectors.toList());

                    // Find all phases
                    Iterable<Phase> phaseIterable = phaseRepository.findAll();
                    List<Phase> allPhases = StreamSupport.stream(phaseIterable.spliterator(), false)
                        .collect(Collectors.toList());

                    // Find Phase names and respective Grades
                    List<Grade> grades = gradeRepository.findGradeByTeamId(teamId);

                    // Map grades to their respective phases
                    Map<Long, Grade> gradeMap = grades.stream()
                        .collect(Collectors.toMap(Grade::getPhase_id, grade -> grade));

                    // Prepare the list of RetrieveGradeDto with default grade as 0
                    List<RetrieveGradeDto> gradesDto = new ArrayList<>();
                    
                    for (Phase phase : allPhases) {
                        RetrieveGradeDto gradeDto = new RetrieveGradeDto();
                        gradeDto.setPhaseName(phase.getName());

                        if (gradeMap.containsKey(phase.getId())) {
                            // If there is a grade for this phase, set it
                            gradeDto.setGrade(gradeMap.get(phase.getId()).getGrade());
                        } else {
                            // Otherwise, set grade to 0
                            gradeDto.setGrade(0);
                        }
                        gradesDto.add(gradeDto);
                    }

                    // Response DTO
                    RetrieveProjectDetailsDto dto = new RetrieveProjectDetailsDto();
                    dto.setTeamId(teamId);
                    dto.setTeamName(team.getName());
                    dto.setActive(team.getActive());
                    dto.setAdvisorId(advisorId);
                    dto.setMembers(memberDtos);
                    dto.setGrades(gradesDto);

                    return dto;
                } else {
                    throw new AuthenticationCredentialsNotFoundException("Team not found!");
                }

            } else {
                // User not Found
                throw new AuthenticationCredentialsNotFoundException("Usuário não encontrado para o email fornecido.");
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw new AuthenticationCredentialsNotFoundException("Token JWT inválido.");
        } catch (AuthenticationCredentialsNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Ocorreu um erro ao recuperar os detalhes do projeto.", e);
        }
    }


}
