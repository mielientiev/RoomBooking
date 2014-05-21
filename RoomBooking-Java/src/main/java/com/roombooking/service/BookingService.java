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
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        return bookings.isEmpty() ? null : bookings;
    }

    public List<Booking> getAvailableBookingsByUserId(int id) {
        List<Booking> bookings = bookingDao.getAvailableBookingsByUserId(id);
        return bookings.isEmpty() ? null : bookings;
    }

    public List<Booking> getBookingByRoomIdAndDate(int id, String date) {
        Date convertedDate;
        try {
            convertedDate = Date.valueOf(date);
        } catch (IllegalStateException e) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return bookingDao.getBookingsByRoomIdAndDate(id, convertedDate);
    }

    public Booking addBooking(User user, Date date, int roomId, int timetableId) {
        List<Booking> list = bookingDao.getBookingsByRoomIdDateAndTimetableId(date, roomId, timetableId);
        if (!list.isEmpty()) {
            logger.debug("Booking with roomId {}, date = {} and timeId {} exists", roomId, date, timetableId);
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        Timetable timetable = timetableDao.findById(timetableId);
        if (timetable == null || !canBookAtThisTimeAndDate(date, timetable.getStart())) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        Room room = roomDao.getRoomByIdWithUserRights(roomId, user);
        if (room == null) {
            logger.debug("Room with bookingId#{} doesn't exist", roomId);
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        if (!room.getRoomType().getRights().iterator().next().getCanBookRoom()) {
            logger.debug("User haven't rights to book this room! ", roomId);
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }

        Booking booking = new Booking();
        booking.setDate(date);
        booking.setRoom(room);
        booking.setTimetable(timetable);
        booking.setUser(user);
        bookingDao.save(booking);
        return booking;
    }

    /**
     * Check if user can book room at this date and time
     *
     * @param bookingDate        - date when user want to book room
     * @param timeOfLectureStart - time of lecture start (timetable.getStart())
     * @return if `date` more than NOW returns true. If `date` == NOW and current time < time - returns true else false
     */
    private boolean canBookAtThisTimeAndDate(Date bookingDate, Time timeOfLectureStart) {
        String date = bookingDate.toString();
        String now = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        Calendar calendar = Calendar.getInstance();
        Time currentTime = Time.valueOf(
                calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                        calendar.get(Calendar.MINUTE) + ":" +
                        calendar.get(Calendar.SECOND)
        );

        return date.compareTo(now) >= 0 && !(date.compareTo(now) == 0 && currentTime.after(timeOfLectureStart));
    }

    public void deleteUserBooking(int bookingId, User user) {
        Booking booking = bookingDao.findById(bookingId);
        if (booking == null) {
            logger.debug("Booking with bookingId#{} doesn't exist", bookingId);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        Timetable timetable = booking.getTimetable();
        if (timetable == null || !canBookAtThisTimeAndDate(booking.getDate(), timetable.getStart())) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        if (!booking.getUser().equals(user)) {
            logger.debug("This user cannot delete booking #{}", bookingId);
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }

        bookingDao.deleteById(bookingId);
    }

    public List<Booking> filterUserBookingsByDate(int userId, String dateFrom, String dateTo) {
        Date fromDate, toDate;
        try {
            fromDate = Date.valueOf(dateFrom);
            toDate = Date.valueOf(dateTo);
        } catch (IllegalStateException e) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        if (fromDate.after(toDate)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        return bookingDao.filterUserBookingsByDate(userId, fromDate, toDate);
    }
}
