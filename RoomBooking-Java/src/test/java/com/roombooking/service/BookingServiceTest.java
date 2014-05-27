package com.roombooking.service;

import com.roombooking.dao.booking.BookingDao;
import com.roombooking.entity.Booking;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class BookingServiceTest {
    private  BookingService bookingService;
    private  BookingDao bookingDao;

    @Before
    public void setUp(){
        bookingService = new BookingService();
        bookingDao = mock(BookingDao.class);
        bookingService.bookingDao = bookingDao;
    }

    @Test
    public void testGetAllBookingsByUserId(){
        when(bookingService.bookingDao.getAllBookingsByUserId(anyInt())).thenReturn(anyListOf(Booking.class));
        bookingService.getAllBookingsByUserId(1);
        verify(bookingService.bookingDao).getAllBookingsByUserId(anyInt());
    }

    @Test(expected = WebApplicationException.class)
    public void testGetBookingByRoomIdAndWrongDate()  {
        bookingService.getBookingByRoomIdAndDate(1,"2014-00-00");
    }

    @Test
    public void testGetBookingByRoomIdAndRightDate() {
        List<Booking> list = Arrays.asList(mock(Booking.class));
        when(bookingService.bookingDao.getBookingsByRoomIdAndDate(1, Date.valueOf("2014-05-05"))).thenReturn(list);
        bookingService.getBookingByRoomIdAndDate(1, "2014-05-05");
        verify(bookingService.bookingDao).getBookingsByRoomIdAndDate(1, Date.valueOf("2014-05-05"));
    }

    @Test
    public void testGetAvailableBookingsByUserId(){
        when(bookingService.bookingDao.getAvailableBookingsByUserId(anyInt())).thenReturn(anyListOf(Booking.class));
        bookingService.getAvailableBookingsByUserId(1);
        verify(bookingService.bookingDao).getAvailableBookingsByUserId(anyInt());
    }

}