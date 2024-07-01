package com.innov8ors.backend.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.innov8ors.backend.repository.UserRepository;
import com.innov8ors.backend.model.User;
import com.innov8ors.backend.model.UserDetailsImpl;;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            User userEntity = user.get();
            UserDetailsImpl userDetailsImpl = new UserDetailsImpl(userEntity);
            // Loading User Roles
            Collection<? extends GrantedAuthority> authorities = userDetailsImpl.getAuthorities(); 
            return new org.springframework.security.core.userdetails.User(userEntity.getEmail(), userEntity.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("Usuário não encontrado com o email " + username);
        }
    }
    
}
