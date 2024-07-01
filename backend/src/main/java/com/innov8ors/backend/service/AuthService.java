package com.innov8ors.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.innov8ors.backend.dto.RecoveryTokenDto;
import com.innov8ors.backend.dto.UserLoginDto;
import com.innov8ors.backend.model.User;
import com.innov8ors.backend.repository.UserRepository;
import com.innov8ors.backend.security.config.JwtTokenProvider;

@Service
public class AuthService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    public RecoveryTokenDto loginUser(UserLoginDto userLogin){
        // Verify Password
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername((String)userLogin.getEmail());
        if (!passwordEncoder.matches((CharSequence) userLogin.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("Senha incorreta");
        }

        // Create User response
        User user_response = new User(null, userDetails.getUsername(), null, userDetails.getAuthorities().toString());

        // Generate Token
        RecoveryTokenDto recoveryToken = new RecoveryTokenDto();
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        String token = jwtTokenProvider.generateToken(userDetails);
        recoveryToken.setToken(token);
        recoveryToken.setUser(user_response); // Send User details for debug purpose

        return recoveryToken;
    }

    public void changePassword(String authorizationHeader, String newPassword) throws Exception {
        String jwtToken = authorizationHeader.substring(7); // Remove "Bearer "
        String email = JwtTokenProvider.getEmailFromToken(jwtToken);
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new AuthenticationCredentialsNotFoundException("User not found for the provided email.");
        }
    }
}
