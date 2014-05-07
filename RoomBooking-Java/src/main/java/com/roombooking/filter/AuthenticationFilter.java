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
import java.util.Optional;

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
        Optional<User> user = userAuthorization(authorization);
        if (!user.isPresent()) {
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }

        boolean secure = requestContext.getSecurityContext().isSecure();
        requestContext.setSecurityContext(new AuthSecure(new AuthPrincipal(user.get()), secure));
        servletRequest.setAttribute("CurrentUser",user.get());
        return requestContext;
    }

    private Optional<User> userAuthorization(String auth) {
        if (auth == null) {
            return Optional.empty();
        }

        String decodedAuth = decode(auth);
        if (decodedAuth.isEmpty())
            return Optional.empty();

        int pos = decodedAuth.indexOf(":");    //todo
        String login = decodedAuth.substring(0, pos);
        String password = decodedAuth.substring(pos + 1);
        return Optional.ofNullable(dao.findByLoginPassword(login, password));
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