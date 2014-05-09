package com.roombooking.filter;

import com.roombooking.dao.user.UserDao;
import com.roombooking.entity.User;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.nio.charset.Charset;

import static javax.ws.rs.core.Response.Status;

@Service
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Autowired
    private UserDao dao;

    @Context
    private HttpServletRequest servletRequest;

    @Override
    public ContainerRequest filter(ContainerRequest requestContext) {
        String authorization = requestContext.getRequestHeaders().getFirst("Authorization");
        User user = userAuthorization(authorization);
        if (user == null) {
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }

        boolean secure = requestContext.getSecurityContext().isSecure();
        requestContext.setSecurityContext(new AuthSecure(new AuthPrincipal(user), secure));
        servletRequest.setAttribute("CurrentUser", user);
        return requestContext;
    }

    private User userAuthorization(String auth) {
        if (auth == null) {
            return null;
        }

        String decodedAuth = decode(auth);
        if (decodedAuth.isEmpty())
            return null;

        int pos = decodedAuth.indexOf(":");  //todo validate [:] char
        String login = decodedAuth.substring(0, pos);
        String password = decodedAuth.substring(pos + 1);
        return dao.findByLoginPassword(login, password);
    }

    private String decode(String auth) {
        auth = auth.replaceFirst("Basic ", "");
        String decodedAuth = new String(Base64.decodeBase64(auth.trim()), Charset.forName("UTF-8"));
        int pos = decodedAuth.indexOf(":");
        if (pos <= 0) {
            return "";
        }
        return decodedAuth;

    }

}