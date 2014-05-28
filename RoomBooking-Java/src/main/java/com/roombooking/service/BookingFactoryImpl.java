package com.roombooking.service;

import com.roombooking.auth.Roles;
import com.roombooking.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class BookingFactoryImpl implements BookingFactory {

    @Autowired
    @Qualifier("bookingService")
    private BookingService bookingService;

    @Autowired
    @Qualifier("adminBookingService")
    private BookingService adminBookingService;

    @Override
    public BookingService createBookingService(User user) {
        switch (user.getRole().getTitle()) {
            case Roles.ADMIN:
                return adminBookingService;
            case Roles.USER:
                return bookingService;
            default:
                throw new RuntimeException("Booking service for user role: " + user.getRole().getTitle() + " doesnt exist");
        }
    }
}
