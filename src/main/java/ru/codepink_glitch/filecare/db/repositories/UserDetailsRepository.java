package ru.codepink_glitch.filecare.db.repositories;

import ru.codepink_glitch.filecare.db.entities.UserDetailsImpl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetailsImpl, Long> {

    Optional<UserDetailsImpl> findUserDetailsImplByUsername(String username);

    Boolean existsByUsernameEqualsIgnoreCase(String username);
}
