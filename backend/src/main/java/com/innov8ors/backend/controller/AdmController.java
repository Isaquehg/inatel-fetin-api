package com.innov8ors.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innov8ors.backend.dto.CreateTeamDto;
import com.innov8ors.backend.dto.CreateUserDto;
import com.innov8ors.backend.dto.RetrieveAllPhasesDto;
import com.innov8ors.backend.dto.RetrieveAllTeamsDto;
import com.innov8ors.backend.dto.RetrieveProjectDetailsDto;
import com.innov8ors.backend.dto.RetrieveTeamsByPhaseDto;
import com.innov8ors.backend.dto.UpdatePhaseDto;
import com.innov8ors.backend.dto.UpdateTeamAdvisorDto;
import com.innov8ors.backend.dto.UpdateTeamGradeDto;
import com.innov8ors.backend.dto.UpdateTeamNameDto;
import com.innov8ors.backend.dto.UpdateTeamStatusDto;
import com.innov8ors.backend.service.AdmService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@RestController
@CrossOrigin
@RequestMapping("/api/v1/adm")
public class AdmController {

    @Autowired
    AdmService admService;

    @GetMapping("/fetin-stages")
    @Operation(summary = "Get all FETIN stages", description = "Retrieves all FETIN stages.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all stages"),
        @ApiResponse(responseCode = "500", description = "Failed to retrieve stages")
    })
    public ResponseEntity<?> fetinStages() {
        try {
            RetrieveAllPhasesDto phasesDto = admService.findAllPhases();
            return ResponseEntity.status(HttpStatus.OK).body(phasesDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve phases: " + e.getMessage());
        }
    }

    @GetMapping("/fetin-stages/{phaseId}")
    @Operation(summary = "Get teams by stage", description = "Retrieves teams by stage ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved teams by stage"),
        @ApiResponse(responseCode = "500", description = "Failed to retrieve teams by stage")
    })
    public ResponseEntity<?> teamsByStage(@Parameter(description = "ID of the stage", required = true) @PathVariable("phaseId") Long phaseId) {
        try {
            RetrieveTeamsByPhaseDto teamsByPhaseDto = admService.findTeamsByPhase(phaseId);
            return ResponseEntity.status(HttpStatus.OK).body(teamsByPhaseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve teams by phase: " + e.getMessage());
        }
    }

    @PostMapping("/teams/register")
    @Operation(summary = "Register new team", description = "Registers a new team.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Team created"),
        @ApiResponse(responseCode = "500", description = "Failed to create team")
    })
    public ResponseEntity<?> registerNewTeam(@RequestBody CreateTeamDto newTeam) {
        try {
            admService.registerNewTeam(newTeam);
            return ResponseEntity.status(HttpStatus.CREATED).body("Team created!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create team: " + e.getMessage());
        }
    }

    @GetMapping("/teams")
    @Operation(summary = "Get all teams", description = "Retrieves all teams.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all teams"),
        @ApiResponse(responseCode = "500", description = "Failed to retrieve teams")
    })
    public ResponseEntity<?> teams(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            RetrieveAllTeamsDto teamsDto = admService.findAllTeams();
            return ResponseEntity.status(HttpStatus.OK).body(teamsDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve teams: " + e.getMessage());
        }
    }

    @GetMapping("/teams/{teamId}")
    @Operation(summary = "Get team by ID", description = "Retrieves a team by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the team"),
        @ApiResponse(responseCode = "500", description = "Failed to retrieve team by ID")
    })
    public ResponseEntity<?> findTeamById(@Parameter(description = "ID of the team", required = true) @PathVariable("teamId") Long teamId) {
        try {
            RetrieveProjectDetailsDto teamDetailsDto = admService.findTeamById(teamId);
            return ResponseEntity.status(HttpStatus.OK).body(teamDetailsDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve team by ID: " + e.getMessage());
        }
    }

    @PutMapping("/teams/{teamId}/name")
    @Operation(summary = "Update team name", description = "Updates the name of a specified team.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Team name updated successfully"),
        @ApiResponse(responseCode = "400", description = "Failed to update team name")
    })
    public ResponseEntity<String> updateTeamName(
            @Parameter(description = "ID of the team to be updated", required = true) @PathVariable("teamId") Long teamId,
            @RequestBody UpdateTeamNameDto updateTeamNameDto) {
        try {
            admService.updateTeamName(teamId, updateTeamNameDto.getNewTeamName());
            return ResponseEntity.ok("Team name updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update team name: " + e.getMessage());
        }
    }

    @PutMapping("/teams/{teamId}/advisor")
    @Operation(summary = "Update team advisor", description = "Updates the advisor of a specified team.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Team advisor updated successfully"),
        @ApiResponse(responseCode = "400", description = "Failed to update team advisor")
    })
    public ResponseEntity<String> updateTeamAdvisor(
            @Parameter(description = "ID of the team to be updated", required = true) @PathVariable("teamId") Long teamId,
            @RequestBody UpdateTeamAdvisorDto updateTeamAdvisorDto) {
        try {
            admService.updateTeamAdvisor(teamId, updateTeamAdvisorDto.getNewAdvisor());
            return ResponseEntity.ok("Team advisor updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update team advisor: " + e.getMessage());
        }
    }

    @PostMapping("/teams/{teamId}/members")
    @Operation(summary = "Add team member", description = "Adds a new member to the specified team.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Member added successfully"),
        @ApiResponse(responseCode = "400", description = "Failed to add member"),
        @ApiResponse(responseCode = "500", description = "An unexpected error occurred")
    })
    public ResponseEntity<String> addTeamMember(
            @Parameter(description = "ID of the team to which the member will be added", required = true) @PathVariable("teamId") Long teamId,
            @RequestBody CreateUserDto memberDto) {
        try {
            admService.addTeamMember(teamId, memberDto);
            return ResponseEntity.ok("Member added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add member: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/teams/{teamId}/members") 
    @Operation(summary = "Remove team member", description = "Removes a member from the specified team.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Member removed successfully"),
        @ApiResponse(responseCode = "400", description = "Failed to remove member"),
        @ApiResponse(responseCode = "500", description = "An unexpected error occurred")
    })
    public ResponseEntity<String> removeTeamMember(
            @Parameter(description = "ID of the team from which the member will be removed", required = true) @PathVariable("teamId") Long teamId,
            @RequestParam("memberEmail") String memberEmail) {
        try {
            admService.removeTeamMember(teamId, memberEmail);
            return ResponseEntity.ok("Member removed successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to remove member: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/teams/{teamId}/grade")
    @Operation(summary = "Add team grade", description = "Adds a grade for the specified team in a specified phase.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Grade added successfully"),
        @ApiResponse(responseCode = "400", description = "Failed to add grade")
    })
    public ResponseEntity<String> addTeamGrade(
            @Parameter(description = "ID of the team to which the grade will be added", required = true) @PathVariable("teamId") Long teamId,
            @RequestBody UpdateTeamGradeDto updateTeamGradeDto) {
        try {
            admService.addTeamGrade(teamId, updateTeamGradeDto.getPhaseId(), updateTeamGradeDto.getGrade());
            return ResponseEntity.ok("Grade added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add grade: " + e.getMessage());
        }
    }

    @PutMapping("/teams/{teamId}/status")
    @Operation(summary = "Update team status", description = "Updates the status of the specified team.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Team status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Failed to update team status")
    })
    public ResponseEntity<String> updateTeamStatus(
            @Parameter(description = "ID of the team to be updated", required = true) @PathVariable("teamId") Long teamId,
            @RequestBody UpdateTeamStatusDto updateTeamStatusDto) {
        try {
            admService.updateTeamStatus(teamId, updateTeamStatusDto.getIsActive());
            return ResponseEntity.ok("Team status updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update team status: " + e.getMessage());
        }
    }

    @PostMapping("/teams/{teamId}/set-responsible-member")
    @Operation(summary = "Set responsible member", description = "Sets a member as responsible for the specified team.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Member set as responsible for the team"),
        @ApiResponse(responseCode = "400", description = "Failed to set member as responsible for the team")
    })
    public ResponseEntity<String> setResponsibleMember(
            @Parameter(description = "ID of the team", required = true) @PathVariable("teamId") Long teamId,
            @RequestParam("memberEmail") String memberEmail) {
        try {
            admService.setResponsibleMember(teamId, memberEmail);
            return ResponseEntity.ok("Member set as responsible for the team.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to set member as responsible for the team: " + e.getMessage());
        }
    }

    @PutMapping("/phases/{phaseId}")
    @Operation(summary = "Update phase", description = "Updates a specified phase.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Phase updated successfully"),
        @ApiResponse(responseCode = "400", description = "Failed to update phase")
    })
    public ResponseEntity<String> updatePhase(
            @Parameter(description = "ID of the phase to be updated", required = true) @PathVariable("phaseId") Long phaseId,
            @RequestBody UpdatePhaseDto phaseUpdateRequest) {
        try {
            admService.updatePhase(phaseId, phaseUpdateRequest);
            return ResponseEntity.ok("Phase updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update phase: " + e.getMessage());
        }
    }
}
