package com.roombooking.restapi;

import com.roombooking.entity.User;
import com.roombooking.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Component
@Path("/user-service")
public class UserResource {

    private static final Logger logger = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private UserService userService;

    @GET
    @Path("/user/{id}")
    @RolesAllowed({"Admin", "User"})
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserById(@PathParam("id") int id) {
        logger.debug("Get user by id = " + id);
        User user = userService.getUserById(id);
        if (user == null) {
            logger.debug("User #" + id + " Not Found");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        logger.debug("User #" + id + " Found");
        return user;
    }

    @GET
    @Path("/user/")
    @RolesAllowed({"Admin", "User"})
    @Produces(MediaType.APPLICATION_JSON)
    public User getCurrentUser(@Context HttpServletRequest servletRequest) {
        logger.debug("Get current user");
        User user = (User) servletRequest.getAttribute("CurrentUser");
        if (user == null) {
            logger.debug("User Not Found");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        User currentUser = userService.getUserById(user.getId());
        logger.debug("User Found");
        return currentUser;
    }

    @GET
    @Path("/users")
    @RolesAllowed("Admin")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers() {
        logger.debug("Get All Users ");
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            logger.debug("Users Not Found");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        logger.debug("Users Found");
        return users;
    }

}
