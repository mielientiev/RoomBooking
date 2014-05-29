package com.roombooking.restapi;

import com.roombooking.entity.Role;
import com.roombooking.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.roombooking.auth.Roles.ADMIN;
import static com.roombooking.auth.Roles.USER;

@Component
@Path("/roles")
public class RoleResource {

    @Autowired
    private RoleService roleService;

    @GET
    @RolesAllowed({ADMIN, USER})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Role> getRoles() {
        List<Role> roles = roleService.getAllRoles();
        if (roles.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return roles;
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({ADMIN, USER})
    @Produces(MediaType.APPLICATION_JSON)
    public Role getRoleById(@PathParam("id") int id) {
        Role role = roleService.getRoleById(id);
        if (role == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return role;
    }

    @PUT
    @RolesAllowed({ADMIN})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Role addNewRole(Role role) {
        if (!isValidRole(role)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return roleService.addRole(role);
    }

    private boolean isValidRole(Role role) {
        return !(role == null || role.getTitle() == null);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({ADMIN})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteRole(@PathParam("id") int id) {
        roleService.deleteRole(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}")
    @RolesAllowed({ADMIN})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Role editRole(@PathParam("id") int id, Role role) {
        if (!isValidRole(role)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return roleService.editRole(id, role);
    }

}
