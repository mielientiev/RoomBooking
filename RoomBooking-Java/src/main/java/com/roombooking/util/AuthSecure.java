package com.roombooking.util;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class AuthSecure implements SecurityContext {

    private final AuthPrincipal authPrincipal;
    private final boolean secure;

    public AuthSecure(AuthPrincipal authPrincipal, boolean secure) {
        this.secure = secure;
        this.authPrincipal = authPrincipal;
    }

    @Override
    public Principal getUserPrincipal() {
        return authPrincipal;
    }

    @Override
    public boolean isUserInRole(String role) {
        return role.equalsIgnoreCase("ADMIN")
                && authPrincipal.getUser() != null && authPrincipal.getUser().getPosition() == 1;
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public String getAuthenticationScheme() {
        return BASIC_AUTH;
    }
}
