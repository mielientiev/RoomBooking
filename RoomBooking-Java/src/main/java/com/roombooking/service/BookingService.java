package com.roombooking.service;

import com.roombooking.dao.booking.BookingDao;
import com.roombooking.dao.room.RoomDao;
import com.roombooking.dao.timetable.TimetableDao;
import com.roombooking.entity.Booking;
import com.roombooking.entity.Room;
import com.roombooking.entity.Timetable;
import com.roombooking.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private BookingDao bookingDao;

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private TimetableDao timetableDao;

    public List<Booking> getAllBookingsByUserId(int id) {
        List<Booking> bookings = bookingDao.getAllBookingsByUserId(id);
        if (!bookings.isEmpty()) {
            cleanUnnecessaryFields(bookings);
        }
        return bookings.isEmpty() ? null : bookings;
    }

    private void cleanUnnecessaryFields(List<Booking> bookings) {
        for (Booking booking : bookings) {
            booking.setUser(null);                                    //todo change this on JsonView
        }
    }

    public List<Booking> getAvailableBookingsByUserId(int id) {
        List<Booking> bookings = bookingDao.getAvailableBookingsByUserId(id);
        if (!bookings.isEmpty()) {
            cleanUnnecessaryFields(bookings);
        }
        return bookings.isEmpty() ? null : bookings;
    }

    public List<Booking> getBookingByRoomIdAndDate(int id, String date) {
        Date convertedDate;
        try {
            convertedDate = Date.valueOf(date);
        } catch (IllegalStateException e) {
            return Collections.emptyList();
        }
        List<Booking> bookings = bookingDao.getBookingsByRoomIdAndDate(id, convertedDate);
        cleanRoom(bookings);
        return bookings;
    }

    private void cleanRoom(List<Booking> bookings) {
        for (Booking booking : bookings) {
            booking.setRoom(null);                                    //todo change this on JsonView
        }
    }

    public Booking addBooking(User user, Date date, int roomId, int timetableId) {
        List<Booking> list = bookingDao.getBookingsByRoomIdDateAndTimetableId(date, roomId, timetableId);
        if (!list.isEmpty()) {
            logger.debug("Booking with roomId {}, date = {} and timeId {} exists", roomId, date, timetableId);
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Room room = roomDao.findById(roomId);
        if (room == null) {
            logger.debug("Room with bookingId#{} doesn't exist", roomId);
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Timetable timetable = timetableDao.findById(timetableId);
        if (timetable == null) {
            logger.debug("Timetable with bookingId#{} doesn't exist", timetableId);
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Booking booking = new Booking();
        booking.setDate(date);
        booking.setRoom(room);
        booking.setTimetable(timetable);
        booking.setUser(user);
        bookingDao.save(booking);
        return booking;
    }

    public void deleteUserBooking(int bookingId, User user) {
        Booking booking = bookingDao.findById(bookingId);
        if (booking == null) {
            logger.debug("Booking with bookingId#{} doesn't exist", bookingId);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        String bookingDate = booking.getDate().toString();
        String now = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        if (bookingDate.compareTo(now) < 0) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }

        if (!booking.getUser().equals(user)) {
            logger.debug("This user cannot delete booking #{}", bookingId);
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }

        bookingDao.deleteById(bookingId);
    }

}
