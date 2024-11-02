package com.naveen.stayease.service;

import com.naveen.stayease.dto.BookingRequest;
import com.naveen.stayease.entity.Booking;
import com.naveen.stayease.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface IBookingService {
    Booking bookRoom(User user, BookingRequest bookingRequest);
    void cancelBooking(User user, long bookingId);
}
