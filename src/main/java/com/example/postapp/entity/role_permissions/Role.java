package com.example.postapp.entity.role_permissions;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static com.example.postapp.entity.role_permissions.Permission.USER_READ;
import static com.example.postapp.entity.role_permissions.Permission.USER_WRITE;
import static java.util.stream.Collectors.toSet;

public enum Role {

    USER(Sets.newHashSet(USER_READ)),
    ADMIN(Sets.newHashSet(USER_WRITE, USER_READ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions () {
        return permissions;
    }

    public Set<GrantedAuthority> getAuthorities () {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(toSet());
    }
}
