package com.example.filecare.db.entities;

import com.example.filecare.enums.UserRole;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * pretty self-explanatory @entity class (needed for spring security)
 */

@Entity
@Data
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

    public AuthorityImpl() {
        this.authority = UserRole.USER;
    }
}
