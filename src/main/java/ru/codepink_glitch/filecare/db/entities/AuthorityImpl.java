package ru.codepink_glitch.filecare.db.entities;

import ru.codepink_glitch.filecare.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * pretty self-explanatory @entity class (needed for spring security)
 */

@Entity
@Data
@NoArgsConstructor
@Table(name = "authority")
public class AuthorityImpl implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority_name", nullable = false)
    private UserRole authority;

    @ManyToOne
    @JoinColumn(name = "user_details_id", nullable = false)
    private UserDetailsImpl userDetails;

    @Override
    public String getAuthority() {
        return this.authority.name();
    }

    public AuthorityImpl(UserRole authority, UserDetailsImpl userDetails) {
        this.authority = authority;
        this.userDetails = userDetails;
    }

}
