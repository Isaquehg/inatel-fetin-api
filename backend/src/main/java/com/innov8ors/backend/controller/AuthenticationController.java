package com.innov8ors.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innov8ors.backend.dto.RecoveryTokenDto;
import com.innov8ors.backend.dto.UserLoginDto;
import com.innov8ors.backend.service.AuthService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserLoginDto userLogin){
        try {
            RecoveryTokenDto recoveryToken = authService.loginUser(userLogin);

            // Build Response
            Map<String, Object> response = new HashMap<>();
            response.put("Message", "Autenticado com sucesso");
            response.put("Bearer", recoveryToken.getToken());
            response.put("User", recoveryToken.getUser()); // Incluindo o usuário na resposta

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
