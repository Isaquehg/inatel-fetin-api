package com.innov8ors.backend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.innov8ors.backend.model.User;

public interface UserRepository extends CrudRepository<User, Long>{
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
