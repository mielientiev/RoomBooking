package com.roombooking.service;

import com.roombooking.entity.Booking;
import com.roombooking.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class AdminBookingService extends BookingService {

    private static final Logger logger = LoggerFactory.getLogger(AdminBookingService.class);

    @Override
    public void deleteBooking(int id, User user) {
        Booking booking = bookingDao.findById(id);
        if (booking == null) {
            logger.debug("Booking with bookingId#{} doesn't exist", id);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        bookingEvent.notify(booking.getRoom().getId(), booking.getDate(), booking.getTimetable().getId(), "canceled");
        bookingDao.deleteById(id);
    }

}
