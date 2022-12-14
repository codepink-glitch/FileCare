package ru.codepink_glitch.filecare.db.entities;

import ru.codepink_glitch.filecare.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

/**
 * pretty self-explanatory @entity class (needed for spring security)
 */

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_details")
public class UserDetailsImpl implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_details_id")
    private Long id;

    @OneToMany(mappedBy = "userDetails", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<AuthorityImpl> authorities;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "non_expired")
    private boolean accountNonExpired = true;

    @Column(name = "non_locked")
    private boolean accountNonLocked = true;

    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired = true;

    @Column(name = "enabled")
    private boolean enabled = true;

    public UserDetailsImpl(String username, String password) {
        this.username = username;
        this.password = password;
        this.authorities = Collections.singletonList(new AuthorityImpl(UserRole.USER, this));
    }

}
