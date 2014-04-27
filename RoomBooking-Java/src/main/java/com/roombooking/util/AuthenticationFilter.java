package com.roombooking.util;

import com.roombooking.entity.User;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import org.apache.commons.codec.binary.Base64;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import java.nio.charset.Charset;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Override
    public ContainerRequest filter(ContainerRequest requestContext) {
        User user = authUser(requestContext);
        if (user == null) {
            throw new WebApplicationException(401);
        }
        boolean secure = requestContext.getSecurityContext().isSecure();
        requestContext.setSecurityContext(new AuthSecure(new AuthPrincipal(user), secure));
        return requestContext;
    }

    private User authUser(ContainerRequest requestContext) {
        String auth = requestContext.getRequestHeaders().getFirst("authorization");
        if (auth == null) {
            return null;
        }

        String decodedAuth = new String(Base64.decodeBase64(auth.trim()), Charset.forName("UTF-8"));
        int pos = decodedAuth.indexOf(":");
        if (pos <= 0) {
            return null;
        }

        String login = decodedAuth.substring(0, pos);
        String password = decodedAuth.substring(pos + 1);

        try {
            return UserDatabase.authenticate(login, password);
        } catch (Exception ex) {
            return null;
        }
    }

}