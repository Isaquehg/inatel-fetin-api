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

import com.innov8ors.backend.dto.RetrieveProjectDetailsDto;
import com.innov8ors.backend.dto.RetrieveTeamsDto;
import com.innov8ors.backend.dto.UpdatePasswordDto;
import com.innov8ors.backend.service.AuthService;
import com.innov8ors.backend.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/member")
public class MemberController {

    @Autowired
    MemberService memberService;

    @Autowired
    AuthService authService;
    
    @GetMapping()
    @Operation(summary = "Get user projects", description = "Retrieves the projects associated with the user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved user projects"),
        @ApiResponse(responseCode = "500", description = "Failed to retrieve user projects")
    })
    public ResponseEntity<RetrieveTeamsDto> userProjects(
            @Parameter(description = "Authorization token", required = true) @RequestHeader("Authorization") String authorizationHeader) {
        try {
            RetrieveTeamsDto teamsDto = memberService.findUserProjects(authorizationHeader);
            return ResponseEntity.status(HttpStatus.OK).body(teamsDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "Get project details", description = "Retrieves details of a specific project associated with the user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved project details"),
        @ApiResponse(responseCode = "500", description = "Failed to retrieve project details")
    })
    public ResponseEntity<RetrieveProjectDetailsDto> projectDetails(
            @Parameter(description = "Authorization token", required = true) @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "ID of the project", required = true) @PathVariable("projectId") int projectId) {
        try {
            RetrieveProjectDetailsDto projectDetailsDto = memberService.findProjectDetails(authorizationHeader, (long) projectId);
            return ResponseEntity.status(HttpStatus.OK).body(projectDetailsDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password", description = "Changes the password for the user.")
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
