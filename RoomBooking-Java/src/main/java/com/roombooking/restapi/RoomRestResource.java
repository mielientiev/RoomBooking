package com.roombooking.restapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.roombooking.entity.Room;
import com.roombooking.entity.User;
import com.roombooking.service.RoomService;
import com.roombooking.util.LoggerUtil;
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
@Path("/room-service")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomRestResource {

    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.getClassName());
    @Autowired
    private RoomService roomService;

    @GET
    @RolesAllowed({"User", "Admin"})
    @Path("/room/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Room getRoomById(@PathParam("id") int id, @Context HttpServletRequest servletRequest) {
        logger.info("Get room by id = " + id);
        User user = (User) servletRequest.getAttribute("CurrentUser");
        Room room = roomService.getRoomByIdWithUserRights(id, user);
        if (room == null) {
            logger.info("Room #" + id + " Not Found");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        logger.info("Room #" + id + " Found");
        return room;
    }

    @GET
    @RolesAllowed({"User","Admin"})
    @Path("/rooms")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getAllRooms(@Context HttpServletRequest servletRequest) {
        User user = (User) servletRequest.getAttribute("CurrentUser");
        logger.info("Get all rooms");
        List<Room> rooms = roomService.getAllRoomsWithUserRights(user);
        if (rooms.isEmpty()) {
            logger.info("Rooms Not Found");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        logger.info("Rooms Found");
        return rooms;
    }

}
