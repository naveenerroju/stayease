package com.naveen.stayease.service;

import com.naveen.stayease.dto.BookingRequest;
import com.naveen.stayease.entity.Booking;
import com.naveen.stayease.entity.Room;
import com.naveen.stayease.entity.User;
import com.naveen.stayease.exception.InvalidInputException;
import com.naveen.stayease.repository.BookingRepository;
import com.naveen.stayease.repository.RoomRepository;
import com.naveen.stayease.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService implements IBookingService{

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Booking bookRoom(User user, BookingRequest bookingRequest) {

        Optional<Room> room = roomRepository.findById(bookingRequest.getRoomId());
        if(room.isEmpty()){
            throw new InvalidInputException("Room doesn't exist with roomId "+bookingRequest.getRoomId());
        }
        if(bookingRequest.getBookingDate().isBefore(LocalDate.now())){
            throw new InvalidInputException("Booking date is not valid.");
        }

        Booking booking = new Booking();
        booking.setRoom(room.get());
        booking.setUser(user);
        booking.setBookingDate(bookingRequest.getBookingDate());

        return bookingRepository.save(booking);
    }

    @Override
    public void cancelBooking(User user, long bookingId) {

    }
}
