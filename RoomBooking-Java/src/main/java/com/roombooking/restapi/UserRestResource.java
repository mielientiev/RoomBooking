package com.roombooking.restapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.roombooking.dao.user.UserDao;
import com.roombooking.entity.User;
import com.roombooking.util.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Component
@Path("/user-service")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRestResource {

    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.getClassName());
    @Autowired
    private UserDao dao;

    @GET
    @RolesAllowed({"Admin", "User"})
    @Path("/user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserById(@PathParam("id") int id) {
        logger.info("Get user by id = " + id);
        User user = dao.getUserById(id);
        if (user == null) {
            logger.info("User #" + id + " Not Found");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        logger.info("User #" + id + " Found");
        return user;
    }

    @GET
    @RolesAllowed("Admin")
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers() {
        logger.info("Get All Users ");
        List<User> users = dao.getAllUsers();
        if (users.isEmpty()) {
            logger.info("Users Not Found");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        logger.info("Users Found");
        return users;
    }


}
