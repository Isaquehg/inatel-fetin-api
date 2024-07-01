package com.innov8ors.backend;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.innov8ors.backend.dto.RecoveryTokenDto;
import com.innov8ors.backend.dto.UserLoginDto;
import com.innov8ors.backend.repository.UserRepository;
import com.innov8ors.backend.service.AuthService;
import com.innov8ors.backend.service.UserDetailsServiceImpl;
import com.innov8ors.backend.model.User;
import com.innov8ors.backend.model.UserDetailsImpl;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTests {

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testLoginUser_SuccessfulAuthentication() {
        // Mock user login data
        UserLoginDto userLogin = new UserLoginDto();
        userLogin.setEmail("email@email.com");
        userLogin.setPassword("password");

        // Mock UserDetails
        UserDetails userDetails = new UserDetailsImpl(new User("Larry", "email@email.com", "hashedPassword", "MEMBER"));
        when(userDetailsService.loadUserByUsername(userLogin.getEmail())).thenReturn(userDetails);

        // Mock behavior of passwordEncoder
        when(passwordEncoder.matches(userLogin.getPassword(), userDetails.getPassword())).thenReturn(true);

        // Call the method under test
        RecoveryTokenDto recoveryToken = authService.loginUser(userLogin);

        // Assert that recoveryToken is not null
        assertNotNull(recoveryToken);

        // Assert that the token is not null or empty
        assertNotNull(recoveryToken.getToken());
    }

    @Test()
    public void testLoginUser_IncorrectPassword() {
        // Mock user login data
        UserLoginDto userLogin = new UserLoginDto();
        userLogin.setEmail("email@email.com");
        userLogin.setPassword("password");

        // Mock UserDetails
        UserDetails userDetails = new UserDetailsImpl(new User("Larry", "email@email.com", "hashedPassword", "MEMBER"));
        when(userDetailsService.loadUserByUsername(userLogin.getEmail())).thenReturn(userDetails);

        // Mock behavior of passwordEncoder
        when(passwordEncoder.matches(userLogin.getPassword(), userDetails.getPassword())).thenReturn(false);

        // Call the method under test - should throw BadCredentialsException
        Assertions.assertThrows(BadCredentialsException.class, () -> authService.loginUser(userLogin));
    }

    @Test()
    public void testLoginUser_UserNotFound() {
        // Mock user login data
        UserLoginDto userLogin = new UserLoginDto();
        userLogin.setEmail("dontexistexample@email.com");
        userLogin.setPassword("passworddoesntexist");

        // Mock behavior of userDetailsService - should throw UsernameNotFoundException
        when(userDetailsService.loadUserByUsername(userLogin.getEmail())).thenThrow(new UsernameNotFoundException("User not found"));

        // Call the method under test
        Assertions.assertThrows(UsernameNotFoundException.class, () -> authService.loginUser(userLogin));
    }

}