package com.roombooking.auth;

import com.roombooking.entity.User;

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
        User user = authPrincipal.getUser();
        return user != null && role.equalsIgnoreCase(user.getRole().getTitle());
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
