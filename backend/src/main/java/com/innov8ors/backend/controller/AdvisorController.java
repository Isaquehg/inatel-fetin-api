package com.innov8ors.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innov8ors.backend.dto.RetrieveTeamsDto;
import com.innov8ors.backend.dto.UpdatePasswordDto;
import com.innov8ors.backend.dto.RetrieveProjectDetailsDto;
import com.innov8ors.backend.service.AdvisorService;
import com.innov8ors.backend.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@RestController
@CrossOrigin
@RequestMapping("/api/v1/advisor")
public class AdvisorController {

    @Autowired
    AdvisorService advisorService;

    @Autowired
    AuthService authService;

    @GetMapping()
    @Operation(summary = "Get tutored projects", description = "Retrieves the projects tutored by the advisor.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved tutored projects"),
        @ApiResponse(responseCode = "500", description = "Failed to retrieve tutored projects")
    })
    public ResponseEntity<RetrieveTeamsDto> tutoredProjects(
            @Parameter(description = "Authorization token", required = true) @RequestHeader("Authorization") String authorizationHeader) {
        try {
            RetrieveTeamsDto teamsDto = advisorService.findTutoredProjects(authorizationHeader);
            return ResponseEntity.status(HttpStatus.OK).body(teamsDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{teamId}")
    @Operation(summary = "Get project details", description = "Retrieves details of a specific project tutored by the advisor.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved project details"),
        @ApiResponse(responseCode = "500", description = "Failed to retrieve project details")
    })
    public ResponseEntity<RetrieveProjectDetailsDto> projectDetails(
            @Parameter(description = "Authorization token", required = true) @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "ID of the team", required = true) @PathVariable("teamId") int teamId) {
        try {
            RetrieveProjectDetailsDto projectDetailsDto = advisorService.findTutoredProjectDetails(authorizationHeader, (long) teamId);
            return ResponseEntity.status(HttpStatus.OK).body(projectDetailsDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password", description = "Changes the password for the advisor.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password changed successfully"),
        @ApiResponse(responseCode = "400", description = "Failed to change password")
    })
    public ResponseEntity<String> changePassword(
            @Parameter(description = "Authorization token", required = true) @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UpdatePasswordDto updatePasswordDto) {
        try {
            authService.changePassword(authorizationHeader, updatePasswordDto.getNewPassword());
            return ResponseEntity.ok("Password changed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to change password: " + e.getMessage());
        }
    }
}
