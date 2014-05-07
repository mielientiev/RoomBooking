package com.roombooking.restapi;

import com.roombooking.dao.user.UserDao;
import com.roombooking.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/role-service")
public class RoleRestResource {

    @Autowired
    private UserDao dao;


    @GET
    @RolesAllowed("User")
    @Path("/role/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Role getRolById(@PathParam("id") int id) {
        return null;
    }

}
