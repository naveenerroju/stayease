package com.naveen.stayease.service;

import com.naveen.stayease.dto.BookingDetails;
import com.naveen.stayease.dto.BookingRequest;
import com.naveen.stayease.entity.User;

import java.util.UUID;

public interface IBookingService {
    BookingDetails bookRoom(User user, BookingRequest bookingRequest);
    void cancelBooking(User user, UUID bookingId);
}
