package com.laterna.xaxathonprime.role.enumeration;

import org.springframework.security.core.GrantedAuthority;

public enum RoleType implements GrantedAuthority {
    FSP_ADMIN,
    REGION_ADMIN,
    USER;

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
