package com.roombooking.restapi;

import com.roombooking.entity.Booking;
import com.roombooking.entity.User;
import com.roombooking.service.BookingService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@Path("/booking")
public class BookingResource {

    private static final Logger logger = LoggerFactory.getLogger(BookingResource.class);

    @Autowired
    private BookingService bookingService;

    @GET
    @RolesAllowed({"Admin", "User"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Booking> getAllBookingByCurrentUser(@Context HttpServletRequest servletRequest) {
        User user = (User) servletRequest.getAttribute("CurrentUser");
        logger.debug("Get all bookings by current user id#{}", user.getId());
        List<Booking> bookings = bookingService.getAllBookingsByUserId(user.getId());
        if (bookings == null) {
            logger.debug("Bookings by current user id#{} not found", user.getId());
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        logger.debug("Bookings by current user id#{} found", user.getId());
        return bookings;
    }

    @GET
    @Path("/available")
    @RolesAllowed({"Admin", "User"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Booking> getAllAvailableBookingByCurrentUser(@Context HttpServletRequest servletRequest) {
        User user = (User) servletRequest.getAttribute("CurrentUser");
        List<Booking> bookings = bookingService.getAvailableBookingsByUserId(user.getId());
        logger.debug("Get all available bookings by current user id#{}", user.getId());
        if (bookings == null) {
            logger.debug("Available bookings by current user id#{} not found", user.getId());
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        logger.debug("Available bookings by current user id#{} found", user.getId());
        return bookings;
    }

    @GET
    @Path("/room-{id}/{date}")
    @RolesAllowed({"Admin", "User"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Booking> getTimetableByRoomAndDate(@PathParam("id") int id, @PathParam("date") String date) {
        logger.debug("Get Booking by room #{} on {}", id, date);
        List<Booking> timetable = bookingService.getBookingByRoomIdAndDate(id, date);
        if (timetable.isEmpty()) {
            logger.debug("Booking by room #{} on {} not found", id, date);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        logger.debug("Booking by room #{} on {} found", id, date);
        return timetable;
    }

    @PUT
    @RolesAllowed({"Admin", "User"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewBooking(Booking booking, @Context HttpServletRequest servletRequest) {
        User user = (User) servletRequest.getAttribute("CurrentUser");
        logger.debug("Add new booking");
        if (booking == null || booking.getDate() == null || booking.getRoom() == null || booking.getTimetable() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        String bookingDate = booking.getDate().toString();
        String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (bookingDate.compareTo(now) < 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Booking createdBooking = bookingService.addBooking(user, booking.getDate(), booking.getRoom().getId(), booking.getTimetable().getId());
        return Response.ok().entity(createdBooking).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"Admin", "User"})
    public Response deleteBooking(@PathParam("id") int id, @Context HttpServletRequest servletRequest) {
        User user = (User) servletRequest.getAttribute("CurrentUser");
        bookingService.deleteUserBooking(id, user);
        return Response.noContent().build();
    }
}
