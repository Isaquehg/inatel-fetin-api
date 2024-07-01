package com.innov8ors.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.innov8ors.backend.dto.CreateTeamDto;
import com.innov8ors.backend.dto.CreateUserDto;
import com.innov8ors.backend.dto.RetrieveAllPhasesDto;
import com.innov8ors.backend.dto.RetrieveAllTeamsDto;
import com.innov8ors.backend.dto.RetrieveGradeDto;
import com.innov8ors.backend.dto.RetrieveMemberDto;
import com.innov8ors.backend.dto.RetrieveProjectDetailsDto;
import com.innov8ors.backend.dto.RetrieveTeamsByPhaseDto;
import com.innov8ors.backend.dto.UpdatePhaseDto;
import com.innov8ors.backend.model.Grade;
import com.innov8ors.backend.model.Member;
import com.innov8ors.backend.model.Phase;
import com.innov8ors.backend.model.Team;
import com.innov8ors.backend.model.User;
import com.innov8ors.backend.repository.GradeRepository;
import com.innov8ors.backend.repository.MemberRepository;
import com.innov8ors.backend.repository.PhaseRepository;
import com.innov8ors.backend.repository.TeamRepository;
import com.innov8ors.backend.repository.UserRepository;

@Service
public class AdmService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    PhaseService phaseService;

    @Autowired
    PhaseRepository phaseRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    GradeRepository gradeRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    UserRepository userRepository;

    public RetrieveAllPhasesDto findAllPhases() {
        Iterable<Phase> phasesIterable = phaseRepository.findAll();
        List<Phase> phases = StreamSupport
                              .stream(phasesIterable.spliterator(), false)
                              .collect(Collectors.toList());
        RetrieveAllPhasesDto phasesDto = new RetrieveAllPhasesDto();
        phasesDto.setPhases(phases);
        return phasesDto;
    }

    public RetrieveTeamsByPhaseDto findTeamsByPhase(Long phaseId) {
        RetrieveTeamsByPhaseDto teamsByPhaseDto = new RetrieveTeamsByPhaseDto();
        Phase currentPhase = phaseService.getCurrentPhase();
        List<RetrieveProjectDetailsDto> teamsList = new ArrayList<>();

        // Verifying if it is the current Phase
        if(currentPhase != null && currentPhase.getId().equals(phaseId)){
            List<Team> activeTeams = teamRepository.findByActive(true);
            for (Team team : activeTeams) {
                List<Member> members = memberRepository.findByTeamId(team.getId());
                    List<Grade> grades = gradeRepository.findGradeByTeamId(team.getId());
                    List<RetrieveMemberDto> membersDto = new ArrayList<>();
                    List<RetrieveGradeDto> gradesDto = new ArrayList<>();
        
                    // Retrieve Members
                    for (Member member : members) {
                        membersDto.add(new RetrieveMemberDto(member.getId(), member.getUser().getName(), member.getUser().getEmail()));
                    }
        
                    // Find Phase names and respective Grades
                    if (!grades.isEmpty()) {
                        for (Grade grade : grades) {
                            Optional<Phase> phaseOptional = phaseRepository.findById(grade.getPhase_id());
                            if (phaseOptional.isPresent()) {
                                Phase phase = phaseOptional.get();
                                RetrieveGradeDto gradeDto = new RetrieveGradeDto();
                                gradeDto.setPhaseName(phase.getName());
                                gradeDto.setGrade(grade.getGrade());
                                gradesDto.add(gradeDto);
                            } else {
                                throw new AuthenticationCredentialsNotFoundException("Phase para determinado Team nao encontrado.");
                            }
                        }
                    } else {
                        throw new IllegalArgumentException("No Grade for this Team yet.");
                    }
                RetrieveProjectDetailsDto projectDetailsDto = new RetrieveProjectDetailsDto();
                projectDetailsDto.setTeamId(team.getId());
                projectDetailsDto.setTeamName(team.getName());
                projectDetailsDto.setAdvisorId(team.getAdvisor().getId());
                projectDetailsDto.setActive(true);
                projectDetailsDto.setMembers(membersDto);
                projectDetailsDto.setGrades(gradesDto);
                teamsList.add(projectDetailsDto);
            }
            teamsByPhaseDto.setTeams(teamsList);
            return teamsByPhaseDto;
        }
        else{
            // Find phase requested
            Optional<Phase> phaseOptional = phaseRepository.findById(phaseId);
            if(phaseOptional.isPresent()){
                RetrieveGradeDto gradeDto = new RetrieveGradeDto();
                List<Grade> gradesByPhase = gradeRepository.findGradeByPhaseId(phaseId);
                // Insert Teams and Grades to DTO
                for (Grade grade : gradesByPhase) {
                    Optional<Team> teamOptional = teamRepository.findById(grade.getTeam_id());
                    if(teamOptional.isPresent()){
                        Team team = teamOptional.get();
                        RetrieveProjectDetailsDto teamDetailsDto = new RetrieveProjectDetailsDto();

                        // Grades
                        gradeDto.setGrade(grade.getGrade());
                        List<RetrieveGradeDto> grades = new ArrayList<>();
                        grades.add(gradeDto);

                        // sub-DTO
                        teamDetailsDto.setTeamId(team.getId());
                        teamDetailsDto.setTeamName(team.getName());
                        teamDetailsDto.setGrades(grades);
                        teamsList.add(teamDetailsDto);
                    }
                }
                teamsByPhaseDto.setTeams(teamsList);
                return teamsByPhaseDto;
            } else{
                throw new IllegalArgumentException("Phase ID is not valid!");
            }
        }
    }

    public void registerNewTeam(CreateTeamDto teamDTO) {
        List<CreateUserDto> userDtos = teamDTO.getMembers();
        CreateUserDto advisorDto = teamDTO.getAdvisor();
        Team globalTeam;

        // Saving new Advisor (if not exists)
        User advisor = userRepository.findByEmail(advisorDto.getEmail())
                                     .orElseGet(() -> {
                                         User newAdvisor = new User(advisorDto.getName(), advisorDto.getEmail(), passwordEncoder.encode("newAdvisor"), "ADVISOR");
                                         return userRepository.save(newAdvisor);
                                     });

        // Save new Team
        Team newTeam = new Team(teamDTO.getTeamName(), true, advisor);
        globalTeam = teamRepository.save(newTeam);

        // Saving new Users and Members (if not exists)
        boolean first_iteration = true;
        for (CreateUserDto createUserDto : userDtos) {
            boolean rFT = first_iteration;
            first_iteration = false;

            User user = userRepository.findByEmail(createUserDto.getEmail())
                                      .orElseGet(() -> {
                                          User newUser = new User(createUserDto.getName(), createUserDto.getEmail(), passwordEncoder.encode("newMember"), "MEMBER");
                                          return userRepository.save(newUser);
                                      });

            // Add Member
            Member newMember = new Member(user, globalTeam, rFT);
            memberRepository.save(newMember);
        }
    }

    public RetrieveAllTeamsDto findAllTeams() {
        RetrieveAllTeamsDto teamsDto = new RetrieveAllTeamsDto();
        Iterable<Team> teamsIterable = teamRepository.findAll();
        List<Team> teams = StreamSupport
                            .stream(teamsIterable.spliterator(), false)
                            .collect(Collectors.toList());

        Map<Long, String> teamsMap = new HashMap<>();
        for (Team team : teams) {
            teamsMap.put(team.getId(), team.getName());
        }

        teamsDto.setTeams(teamsMap);
        return teamsDto;
    }

    public RetrieveProjectDetailsDto findTeamById(Long teamId) {
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
    
            // Find all phases and convert to List
            Iterable<Phase> phaseIterable = phaseRepository.findAll();
            List<Phase> allPhases = StreamSupport.stream(phaseIterable.spliterator(), false)
                .collect(Collectors.toList());
    
            // Map grades to phases
            List<Grade> grades = gradeRepository.findGradeByTeamId(teamId);
            Map<Long, Grade> gradeMap = grades.stream()
                .collect(Collectors.toMap(Grade::getPhase_id, grade -> grade));
    
            List<RetrieveGradeDto> gradesDto = new ArrayList<>();
    
            for (Phase phase : allPhases) {
                RetrieveGradeDto gradeDto = new RetrieveGradeDto();
                gradeDto.setPhaseName(phase.getName());
    
                if (gradeMap.containsKey(phase.getId())) {
                    // If there is a grade for this phase, set it
                    gradeDto.setGrade(gradeMap.get(phase.getId()).getGrade());
                } else {
                    // Otherwise, set a default message
                    gradeDto.setGrade(0);
                }
                gradesDto.add(gradeDto);
            }
    
            // Response DTO
            RetrieveProjectDetailsDto dto = new RetrieveProjectDetailsDto();
            dto.setTeamId(teamId);
            dto.setTeamName(team.getName());
            dto.setActive(team.getActive());
            dto.setAdvisorId(team.getAdvisor().getId());
            dto.setMembers(memberDtos);
            dto.setGrades(gradesDto);
    
            return dto;
        } else {
            throw new IllegalArgumentException("No Team found!");
        }
    }
    

    public void updateTeamName(Long teamId, String newTeamName) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Team not found"));
        team.setName(newTeamName);
        teamRepository.save(team);
    }

    public void updateTeamAdvisor(Long teamId, CreateUserDto newAdvisorDto) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Team not found"));
        User newAdvisor = userRepository.findByEmail(newAdvisorDto.getEmail())
                .orElseGet(() -> {
                    User advisor = new User(newAdvisorDto.getName(), newAdvisorDto.getEmail(), passwordEncoder.encode("newAdvisor"), "ADVISOR");
                    return userRepository.save(advisor);
                });
        team.setAdvisor(newAdvisor);
        teamRepository.save(team);
    }

    public void addTeamMember(Long teamId, CreateUserDto memberDto) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Team not found"));

        List<Member> currentMembers = memberRepository.findByTeamId(teamId);
        if (currentMembers.size() >= 4) {
            throw new IllegalArgumentException("Team already has 4 members");
        }

        User user = userRepository.findByEmail(memberDto.getEmail())
                .orElseGet(() -> {
                    User newUser = new User(memberDto.getName(), memberDto.getEmail(), passwordEncoder.encode("newMember"), "MEMBER");
                    return userRepository.save(newUser);
                });

        boolean memberExists = currentMembers.stream().anyMatch(member -> member.getUser().getEmail().equals(user.getEmail()));
        if (!memberExists) {
            Member newMember = new Member(user, team, false);
            memberRepository.save(newMember);
        } else {
            throw new IllegalArgumentException("Member already exists in the team");
        }
    }

    public void removeTeamMember(Long teamId, String memberEmail) {
        // Verify if Team exists
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Team not found"));

        User user = userRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new IllegalArgumentException("User with the provided email not found"));

        Member member = memberRepository.findByUserAndTeamId(user, teamId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found in the team"));

        memberRepository.delete(member);
    }

    public void addTeamGrade(Long teamId, Long phaseId, int gradeValue) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Team not found"));
        Phase phase = phaseRepository.findById(phaseId).orElseThrow(() -> new IllegalArgumentException("Phase not found"));
        Grade grade = new Grade();
        grade.setTeam(team);
        grade.setPhase(phase);
        grade.setGrade(gradeValue);
        gradeRepository.save(grade);
    }

    public void updateTeamStatus(Long teamId, Boolean isActive) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Team not found"));
        team.setActive(isActive);
        teamRepository.save(team);
    }

    public void setResponsibleMember(Long teamId, String memberEmail) {
        // Verify if Team exists
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
    
        // Verify User
        User user = userRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new IllegalArgumentException("User with the provided email not found"));
    
        // Check if is Member of the Team
        Member member = memberRepository.findByUserAndTeamId(user, teamId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found in the team"));
    
        // Define responsible for Team
        member.setResponsible_for_team(true);
        memberRepository.save(member);
    }

    public void updatePhase(Long phaseId, UpdatePhaseDto phaseUpdateRequest) {
        Phase phase = phaseRepository.findById(phaseId)
                .orElseThrow(() -> new IllegalArgumentException("Phase not found"));

        // Update Phase depending on what is being required
        if (phaseUpdateRequest.getName() != null) {
            phase.setName(phaseUpdateRequest.getName());
        }
        if (phaseUpdateRequest.getStart() != null) {
            phase.setStart(phaseUpdateRequest.getStart());
        }
        if (phaseUpdateRequest.getEnd() != null) {
            phase.setEndDate(phaseUpdateRequest.getEnd());
        }

        phaseRepository.save(phase);
    }

}
