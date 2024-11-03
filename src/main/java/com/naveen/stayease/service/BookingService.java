package com.naveen.stayease.service;

import com.naveen.stayease.dto.BookingDetails;
import com.naveen.stayease.dto.BookingRequest;
import com.naveen.stayease.entity.Booking;
import com.naveen.stayease.entity.Room;
import com.naveen.stayease.entity.User;
import com.naveen.stayease.exception.InvalidInputException;
import com.naveen.stayease.exception.ValidationException;
import com.naveen.stayease.repository.BookingRepository;
import com.naveen.stayease.repository.RoomRepository;
import com.naveen.stayease.repository.UserRepository;
import com.naveen.stayease.util.RoomAvailabilityUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public BookingDetails bookRoom(User user, BookingRequest bookingRequest) {

        Optional<Room> room = roomRepository.findById(bookingRequest.getRoomId());
        if(room.isEmpty()){
            throw new InvalidInputException("Room doesn't exist with roomId "+bookingRequest.getRoomId());
        }
        if(bookingRequest.getBookingDate().isBefore(LocalDate.now())){
            throw new InvalidInputException("Booking date is not valid.");
        }
        //check if the room is available on requested date
        if(RoomAvailabilityUtil.numberOfAvailableRooms(room.get(), bookingRequest.getBookingDate())<1){
            throw new InvalidInputException("The requested room is not available on the requested date. Please choose any available rooms.");
        }
        Booking booking = new Booking();
        booking.setRoom(room.get());
        booking.setUser(user);
        booking.setBookingDate(bookingRequest.getBookingDate());

        Booking bookingResponse  = bookingRepository.save(booking);
        return generateBookingDetails(bookingResponse);
    }

    public List<BookingDetails> getAllBookings(User user){
        List<BookingDetails> bookingDetails = new ArrayList<>();
        for (Booking booking: user.getBookings()){
            bookingDetails.add(generateBookingDetails(booking));
        }
        if(bookingDetails.isEmpty()){
            throw new ValidationException("You do not have any bookings yet");
        }
        return bookingDetails;
    }

    @Override
    @Transactional
    public void cancelBooking(User user, UUID bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if(booking.isEmpty() || !booking.get().getUser().getEmail().equals(user.getEmail())){
            throw new InvalidInputException("Booking Id is invalid");
        }

        if(booking.get().getBookingDate().isBefore(LocalDate.now())){
            throw new ValidationException("This booking is fulfilled");
        }

        bookingRepository.delete(booking.get());
    }

    private BookingDetails generateBookingDetails(Booking bookingResponse){
        return new BookingDetails(bookingResponse.getBookingId(),
                bookingResponse.getRoom().getHotel().getId(),
                bookingResponse.getRoom().getHotel().getName(),
                bookingResponse.getRoom().getId(),
                bookingResponse.getRoom().getName(),
                bookingResponse.getBookingDate());
    }
}
