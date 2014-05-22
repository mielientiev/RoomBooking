package com.roombooking.dao.booking;

import com.roombooking.dao.AbstractDao;
import com.roombooking.entity.Booking;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class BookingJPADao extends AbstractDao<Booking> implements BookingDao {

    protected BookingJPADao() {
        super(Booking.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getAllBookingsByUserId(int id) {
        TypedQuery<Booking> query = getEntityManager().createNamedQuery("Booking.findAllBookingsByUserId", entityClass);
        query.setParameter("userId", id);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getAvailableBookingsByUserId(int id) {
        TypedQuery<Booking> query = getEntityManager().createNamedQuery("Booking.findAllAvailableBookingsByUserId", entityClass);
        query.setParameter("userId", id);
        query.setParameter("date", new java.util.Date());
        query.setParameter("time", new Time(new java.util.Date().getTime()));
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByRoomIdAndDate(int id, Date date) {
        TypedQuery<Booking> query = getEntityManager().createNamedQuery("Booking.findBookingsByRoomIdAndDate", entityClass);
        query.setParameter("roomId", id);
        query.setParameter("date", date);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByRoomIdDateAndTimetableId(Date date, int roomId, int timeId) {
        TypedQuery<Booking> query = getEntityManager().createNamedQuery("Booking.findBookingByRoomIdDateAndTimetableId", entityClass);
        query.setParameter("roomId", roomId);
        query.setParameter("date", date);
        query.setParameter("timeId", timeId);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> filterUserBookingsByDate(int userId, Date fromDate, Date toDate) {
        TypedQuery<Booking> query = getEntityManager().createNamedQuery("Booking.filterBookingByDate", entityClass);
        query.setParameter("dateFrom", fromDate);
        query.setParameter("dateTo", toDate);
        query.setParameter("userId", userId);
        return query.getResultList();

    }

}
