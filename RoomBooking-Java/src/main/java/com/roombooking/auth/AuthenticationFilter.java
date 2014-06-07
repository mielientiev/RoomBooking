package com.roombooking.auth;

import com.roombooking.entity.User;
import com.roombooking.utils.AuthorizationUtil;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status;

@Service
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Autowired
    private AuthorizationUtil authorizationUtil;

    @Context
    private HttpServletRequest servletRequest;

    @Override
    public ContainerRequest filter(ContainerRequest requestContext) {
        String authorization = requestContext.getRequestHeaders().getFirst("Authorization");
        User user = authorizationUtil.userAuthorization(authorization);
        if (user == null) {
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }

        boolean secure = requestContext.getSecurityContext().isSecure();
        requestContext.setSecurityContext(new AuthSecure(new AuthPrincipal(user), secure));
        servletRequest.setAttribute("CurrentUser", user);
        return requestContext;
    }

}