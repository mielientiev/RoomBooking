package com.roombooking.restapi;

import com.fasterxml.jackson.annotation.JsonView;
import com.roombooking.entity.Booking;
import com.roombooking.entity.Room;
import com.roombooking.entity.User;
import com.roombooking.service.BookingFactory;
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
import java.util.List;

import static com.roombooking.auth.Roles.ADMIN;
import static com.roombooking.auth.Roles.USER;

// todo maybe it makes sense to replace BookingService with BookingFactory
@Component
@Path("/booking")
public class BookingResource {

    private static final Logger logger = LoggerFactory.getLogger(BookingResource.class);

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingFactory bookingFactory;

    @GET
    @JsonView({Room.class})
    @RolesAllowed({ADMIN, USER})
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
    @JsonView({Room.class})
    @Path("/available")
    @RolesAllowed({ADMIN, USER})
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
    @JsonView({User.class})
    @Path("/room-{id}/{date}")
    @RolesAllowed({ADMIN, USER})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Booking> getTimetableByRoomAndDate(@PathParam("id") int id, @PathParam("date") String date) {
        logger.debug("Get Booking by room #{} on {}", id, date);
        List<Booking> timetable = bookingService.getBookingsByRoomIdAndDate(id, date);
        if (timetable.isEmpty()) {
            logger.debug("Booking by room #{} on {} not found", id, date);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        logger.debug("Booking by room #{} on {} found", id, date);
        return timetable;
    }

    @GET
    @JsonView({Room.class})
    @Path("/{dateFrom}/{dateTo}")
    @RolesAllowed({ADMIN, USER})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Booking> getUserBookingsByDateRange(@PathParam("dateFrom") String dateFrom,
                                                    @PathParam("dateTo") String dateTo,
                                                    @Context HttpServletRequest servletRequest) {
        User user = (User) servletRequest.getAttribute("CurrentUser");
        logger.debug("Get Booking by dateFrom #{}; dateTo {}", dateFrom, dateTo);
        List<Booking> timetable = bookingService.filterUserBookingsByDate(user.getId(), dateFrom, dateTo);
        if (timetable.isEmpty()) {
            logger.debug("Booking by by dateFrom #{}; dateTo {}", dateFrom, dateTo);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        logger.debug("Booking by dateFrom #{}; dateTo {} found", dateFrom, dateTo);
        return timetable;
    }

    @PUT
    @JsonView({Room.class})
    @RolesAllowed({ADMIN, USER})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewBooking(Booking booking, @Context HttpServletRequest servletRequest) {
        User user = (User) servletRequest.getAttribute("CurrentUser");
        logger.debug("Add new booking");
        if (booking == null || booking.getDate() == null || booking.getRoom() == null || booking.getTimetable() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Booking createdBooking = bookingService.addBooking(user, booking.getDate(), booking.getRoom().getId(), booking.getTimetable().getId());
        return Response.ok().entity(createdBooking).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({ADMIN, USER})
    public Response deleteUserBooking(@PathParam("id") int id, @Context HttpServletRequest servletRequest) {
        User user = (User) servletRequest.getAttribute("CurrentUser");
        BookingService bookingService = bookingFactory.createBookingService(user);
        bookingService.deleteBooking(id, user);
        return Response.noContent().build();
    }

}
