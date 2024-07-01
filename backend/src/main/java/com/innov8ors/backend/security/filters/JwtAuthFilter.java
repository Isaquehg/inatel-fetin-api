package com.innov8ors.backend.security.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.innov8ors.backend.model.User;
import com.innov8ors.backend.model.UserDetailsImpl;
import com.innov8ors.backend.repository.UserRepository;
import com.innov8ors.backend.security.config.JwtTokenProvider;
import com.innov8ors.backend.security.config.SecurityConfig;
import com.innov8ors.backend.service.UserDetailsServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter{

    @Autowired
    private JwtTokenProvider jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Verifica se o endpoint requer autenticação antes de processar a requisição
        String token = recoveryToken(request); // Recupera o token do cabeçalho Authorization da requisição
        System.out.println(request.getRequestURI());
        if (checkIfEndpointIsNotPublic(request)) {
            if (token != null) {
                try {
                    // Validate and extract JWT data
                    Jws<Claims> jwt = jwtTokenService.validateToken(token);
                    Claims claims = jwt.getBody(); // Map<String,Object> {authorities, username}

                    String userEmail = (String) claims.get("username");
                    Optional<User> userOptional = userRepository.findByEmail(userEmail); // Find User

                    if (userOptional.isPresent()) {
                        User user = userOptional.get();
                        // Creates a Authentication Object for Spring Security
                        Authentication authentication =
                                new UsernamePasswordAuthenticationToken(new UserDetailsImpl(user), null, userDetailsServiceImpl.loadUserByUsername(user.getEmail()).getAuthorities());
    
                        // Define Security Object in Spring Security context
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        // User not found
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("Usuario nao encontrado");
                        return;
                    }
                } catch (SignatureException e) {
                    // Invalid token
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token de autenticação invalido");
                    return;
                } 
            } else {
                System.out.println(request.getRequestURI());
                // Missing token...
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("O token esta ausente");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return !Arrays.asList(SecurityConfig.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).contains(requestURI);
    }

    // Recupera o token do cabeçalho Authorization da requisição
    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    /*@Override
    public int getOrder() {
        return 2;
    }*/
}
