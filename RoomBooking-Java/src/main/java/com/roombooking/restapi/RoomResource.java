package com.roombooking.restapi;

import com.roombooking.entity.Room;
import com.roombooking.entity.RoomType;
import com.roombooking.entity.User;
import com.roombooking.service.RoomService;
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
public class RoomResource {

    private static final Logger logger = LoggerFactory.getLogger(RoomResource.class);

    @Autowired
    private RoomService roomService;

    @GET
    @Path("/room/{id}")
    @RolesAllowed({"Admin", "User"})
    @Produces(MediaType.APPLICATION_JSON)
    public Room getRoomById(@PathParam("id") int id, @Context HttpServletRequest servletRequest) {
        logger.debug("Get room by id = {}", id);
        User user = (User) servletRequest.getAttribute("CurrentUser");
        Room room = roomService.getRoomByIdWithUserRights(id, user);
        if (room == null) {
            logger.debug("Room #{} Not Found", id);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        logger.debug("Room #{} Found", id);
        return room;
    }

    @GET
    @Path("/rooms")
    @RolesAllowed({"Admin", "User"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getAllRooms(@Context HttpServletRequest servletRequest) {
        logger.debug("Get all rooms");
        User user = (User) servletRequest.getAttribute("CurrentUser");
        List<Room> rooms = roomService.getAllRoomsWithUserRights(user);
        if (rooms.isEmpty()) {
            logger.debug("Rooms Not Found");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        logger.debug("Rooms Found");
        return rooms;
    }

    @GET
    @Path("/types")
    @RolesAllowed({"Admin", "User"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<RoomType> getAllRoomType() {
        logger.debug("Get all room types");
        List<RoomType> roomTypes = roomService.getAllRoomTypes();
        if (roomTypes.isEmpty()) {
            logger.debug("Room types Not Found");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        logger.debug("Room types Found");
        return roomTypes;
    }

    @GET
    @Path("/type/{roomType}/places/{places}/board/{board}/computers/{computers}/projector/{projector}/name/{roomName}")
    @RolesAllowed({"Admin", "User"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getFilterRooms(@PathParam("roomType") int roomType, @PathParam("places") int places,
                                     @PathParam("board") boolean board, @PathParam("computers") int computers,
                                     @PathParam("projector") boolean projector, @PathParam("roomName") String roomName,
                                     @Context HttpServletRequest servletRequest) {

        logger.debug("Get filter rooms: name-{}, type-{}, places-{}, computers-{}, board-{}, projector-{}", roomName,
                roomType, places, computers, board, projector);
        User user = (User) servletRequest.getAttribute("CurrentUser");
        List<Room> rooms = roomService.filterRooms(user, roomType, places, computers, board, projector, roomName);
        if (rooms.isEmpty()) {
            logger.debug("Filtered Rooms Not Found");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        logger.debug("Filtered Rooms Found");
        return rooms;
    }

    @GET
    @Path("/type/{roomType}/places/{places}/board/{board}/computers/{computers}/projector/{projector}/name/")
    @RolesAllowed({"Admin", "User"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getFilterRoomsWithEmptyName(@PathParam("roomType") int roomType, @PathParam("places") int places,
                                                  @PathParam("board") boolean board, @PathParam("computers") int computers,
                                                  @PathParam("projector") boolean projector,
                                                  @Context HttpServletRequest servletRequest) {

        logger.debug("Get filter rooms (name-empty): name-{}, type-{}, places-{}, computers-{}, board-{}, projector-{}",
                roomType, places, computers, board, projector);
        User user = (User) servletRequest.getAttribute("CurrentUser");
        List<Room> rooms = roomService.filterRooms(user,roomType, places, computers, board, projector, "");
        if (rooms.isEmpty()) {
            logger.debug("Filtered Rooms (name-empty) Not Found");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        logger.debug("Filtered Rooms (name-empty) Found");
        return rooms;
    }

}
