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
import com.innov8ors.backend.dto.RetrieveProjectDetailsDto;
import com.innov8ors.backend.dto.RetrieveTeamDto;
import com.innov8ors.backend.dto.RetrieveTeamsDto;
import com.innov8ors.backend.model.Grade;
import com.innov8ors.backend.model.Member;
import com.innov8ors.backend.model.Phase;
import com.innov8ors.backend.model.Team;
import com.innov8ors.backend.model.User;
import com.innov8ors.backend.repository.GradeRepository;
import com.innov8ors.backend.repository.MemberRepository;
import com.innov8ors.backend.repository.PhaseRepository;
import com.innov8ors.backend.repository.UserRepository;
import com.innov8ors.backend.security.config.JwtTokenProvider;

@Service
public class MemberService {

    @Autowired
    PhaseService phaseService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GradeRepository gradeRepository;

    @Autowired
    PhaseRepository phaseRepository;

    public RetrieveTeamsDto findUserProjects(String authorizationHeader) {
        try {
            // Verifique se o header começa com "Bearer "
            if (!authorizationHeader.startsWith("Bearer ")) {
                throw new AuthenticationCredentialsNotFoundException("Formato do token JWT inválido.");
            }
    
            // Remove "Bearer " do token
            String jwtToken = authorizationHeader.substring(7);
            System.out.println("JWT Token: " + jwtToken);
    
            // Extrai o email do token
            String email = JwtTokenProvider.getEmailFromToken(jwtToken);
            System.out.println("Email extraído do token: " + email);
    
            // Busca o usuário pelo email
            Optional<User> userOptional = userRepository.findByEmail(email);
    
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                System.out.println("Usuário encontrado: " + user.getName());
    
                // Busca os membros associados ao usuário
                List<Member> members = memberRepository.findByUser(user);
                System.out.println("Membros encontrados: " + members.size());
    
                List<RetrieveTeamDto> teams = members.stream()
                    .map(member -> {
                        Team team = member.getTeam();
                        RetrieveTeamDto dto = new RetrieveTeamDto();

                        // Verifica se o advisor não é nulo antes de tentar acessar seus métodos
                        if (team.getAdvisor() != null) {
                            RetrieveAdvisorDto advisorDto = new RetrieveAdvisorDto(team.getAdvisor().getId(), team.getAdvisor().getName(), team.getAdvisor().getEmail());
                            dto.setAdvisor(advisorDto);
                        } else {
                            // Caso o advisor seja nulo, crie um advisor DTO vazio ou com valores padrão
                            RetrieveAdvisorDto advisorDto = new RetrieveAdvisorDto(null, "Advisor não atribuído", "N/A");
                            dto.setAdvisor(advisorDto);
                        }

                        dto.setId(team.getId());
                        dto.setName(team.getName());
                        dto.setActive(team.getActive());

                        return dto;
                    })
                    .collect(Collectors.toList());

    
                // Fase Atual
                Phase currentPhase = phaseService.getCurrentPhase();
                System.out.println("Fase atual: " + currentPhase.getName());
    
                // DTO da Fase
                RetrievePhaseDto phaseDto = new RetrievePhaseDto();
                phaseDto.setPhaseName(currentPhase.getName());
                phaseDto.setStartAt(currentPhase.getStart());
                phaseDto.setEndAt(currentPhase.getEndDate());
    
                RetrieveTeamsDto teamsDto = new RetrieveTeamsDto();
                teamsDto.setFetinPhase(phaseDto);
                teamsDto.setTeams(teams);
    
                return teamsDto;
            } else {
                throw new AuthenticationCredentialsNotFoundException("Usuário não encontrado para o email fornecido.");
            }
        } catch (Exception e) {
            // Log do erro detalhado
            e.printStackTrace();
            throw new RuntimeException("Erro ao processar a requisição", e);
        }
    }
    

    public RetrieveProjectDetailsDto findProjectDetails(String authorizationHeader, Long teamId) {
        try {
            // Extrair e validar o token JWT
            String jwtToken = authorizationHeader.substring(7); // Remove "Bearer "
            String email = JwtTokenProvider.getEmailFromToken(jwtToken);
            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                Optional<Member> memberOptional = memberRepository.findByUserAndTeamId(user, teamId);

                if (memberOptional.isPresent()) {
                    Team team = memberOptional.get().getTeam();
                    
                    // Obter membros da equipe
                    List<Member> members = memberRepository.findByTeamId(teamId);
                    
                    // Obter todas as fases
                    Iterable<Phase> phaseIterable = phaseRepository.findAll();
                    List<Phase> allPhases = StreamSupport.stream(phaseIterable.spliterator(), false)
                        .collect(Collectors.toList());
                    
                    // Obter as notas da equipe
                    List<Grade> grades = gradeRepository.findGradeByTeamId(teamId);

                    // Mapear membros para DTO
                    List<RetrieveMemberDto> membersDto = members.stream()
                        .map(member -> new RetrieveMemberDto(
                            member.getId(),
                            member.getUser().getName(),
                            member.getUser().getEmail()
                        )).collect(Collectors.toList());

                    // Mapear as notas para suas respectivas fases
                    Map<Long, Grade> gradeMap = grades.stream()
                        .collect(Collectors.toMap(Grade::getPhase_id, grade -> grade));

                    // Preparar a lista de RetrieveGradeDto com nota padrão 0
                    List<RetrieveGradeDto> gradesDto = new ArrayList<>();
                    
                    for (Phase phase : allPhases) {
                        RetrieveGradeDto gradeDto = new RetrieveGradeDto();
                        gradeDto.setPhaseName(phase.getName());

                        if (gradeMap.containsKey(phase.getId())) {
                            // Se houver uma nota para esta fase, definir a nota
                            gradeDto.setGrade(gradeMap.get(phase.getId()).getGrade());
                        } else {
                            // Caso contrário, definir a nota como 0
                            gradeDto.setGrade(0);
                        }
                        gradesDto.add(gradeDto);
                    }

                    // Construir o DTO de resposta
                    RetrieveProjectDetailsDto projectDetailsDto = new RetrieveProjectDetailsDto();
                    projectDetailsDto.setTeamId(team.getId());
                    projectDetailsDto.setTeamName(team.getName());
                    projectDetailsDto.setActive(team.getActive());
                    projectDetailsDto.setAdvisorId(team.getAdvisor().getId());
                    projectDetailsDto.setMembers(membersDto);
                    projectDetailsDto.setGrades(gradesDto);
                    
                    return projectDetailsDto;
                } else {
                    throw new IllegalArgumentException("User is not a member of the specified project.");
                }
            } else {
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
