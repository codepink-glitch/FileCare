package com.example.filecare.db.repositories;

import com.example.filecare.db.entities.UserDetailsImpl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetailsImpl, Long> {

    Optional<UserDetailsImpl> findUserDetailsImplByUsername(String username);
}
