package com.roombooking.util;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.*;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.util.Collections;
import java.util.List;

@Provider
public class RolesAllowedResourceFilterFactory implements ResourceFilterFactory {

    @Context
    private SecurityContext securityCtx;

    @Override
    public List<ResourceFilter> create(AbstractMethod method) {
        if (method.isAnnotationPresent(DenyAll.class))
            return Collections.<ResourceFilter>singletonList(new
                    Filter());

        // PermitAll takes precedence over RolesAllowed on the class
        if (method.isAnnotationPresent(PermitAll.class))
            return null;

        // RolesAllowed on the method takes precedence over PermitAll
        RolesAllowed rolesAllowed = method.getAnnotation(RolesAllowed.class);
        if (rolesAllowed != null)
            return Collections.<ResourceFilter>singletonList(new Filter(rolesAllowed.value()));

        // RolesAllowed on the class takes precedence over PermitAll
        rolesAllowed = method.getResource().getAnnotation(RolesAllowed.class);
        if (rolesAllowed != null)
            return Collections.<ResourceFilter>singletonList(new Filter(rolesAllowed.value()));

        return null;
    }

    private class Filter implements ResourceFilter, ContainerRequestFilter {

        private final boolean denyAll;
        private final String[] rolesAllowed;

        protected Filter() {
            this.denyAll = true;
            this.rolesAllowed = null;
        }

        protected Filter(String[] rolesAllowed) {
            this.denyAll = false;
            this.rolesAllowed = (rolesAllowed != null) ?
                    rolesAllowed : new String[]{};
        }

        @Override
        public ContainerRequest filter(ContainerRequest request) {
            if (!denyAll) {
                for (String role : rolesAllowed) {
                    if (securityCtx.isUserInRole(role))
                        return request;
                }
            }
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        @Override
        public ContainerRequestFilter getRequestFilter() {
            return null;
        }

        @Override
        public ContainerResponseFilter getResponseFilter() {
            return null;
        }
    }
}