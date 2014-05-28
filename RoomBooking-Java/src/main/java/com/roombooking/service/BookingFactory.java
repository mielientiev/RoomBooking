package com.roombooking.service;

import com.roombooking.entity.User;

public interface BookingFactory {
    BookingService createBookingService(User user);
}
