package com.roombooking.auth;

import com.roombooking.entity.User;

import java.security.Principal;

public class AuthPrincipal implements Principal {

    private User user;

    public AuthPrincipal(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getName() {
        return user != null ? user.getLogin() : "Nothing";
    }

}
