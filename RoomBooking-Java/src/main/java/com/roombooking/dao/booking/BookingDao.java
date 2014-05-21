package com.roombooking.dao.booking;

import com.roombooking.dao.Dao;
import com.roombooking.entity.Booking;

import java.sql.Date;
import java.util.List;

public interface BookingDao extends Dao<Booking> {

    List<Booking> getAllBookingsByUserId(int id);

    List<Booking> getAvailableBookingsByUserId(int id);

    List<Booking> getBookingsByRoomIdAndDate(int id, Date date);

    List<Booking> getBookingsByRoomIdDateAndTimetableId(Date date, int roomId, int timeId);

    List<Booking> filterUserBookingsByDate(int userId, Date fromDate, Date toDate);

}
