package com.example.postapp.entity;

import com.example.postapp.entity.role_permissions.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

import static com.example.postapp.entity.Status.ACTIVE;
import static javax.persistence.EnumType.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(
            generator = "app_user_generator",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
            name = "app_user_generator",
            sequenceName = "user_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Role role;

    @Transient
    private Set<? extends GrantedAuthority> grantedAuthorities;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Status status;

    @Transient
    private boolean isActive;

    public UserEntity(String name,
                   String email,
                   String password,
                   Role role,
                   Set<? extends GrantedAuthority> grantedAuthorities,
                   Status status) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.grantedAuthorities = grantedAuthorities;
        this.status = status;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status.equals(ACTIVE);
    }

    @Override
    public boolean isAccountNonLocked() {
        return status.equals(ACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status.equals(ACTIVE);
    }

    @Override
    public boolean isEnabled() {
        return status.equals(ACTIVE);
    }

}
