package com.naveen.stayease.service;

import com.naveen.stayease.dto.BookingDetails;
import com.naveen.stayease.dto.BookingRequest;
import com.naveen.stayease.entity.Booking;
import com.naveen.stayease.entity.Hotel;
import com.naveen.stayease.entity.Room;
import com.naveen.stayease.entity.User;
import com.naveen.stayease.exception.InvalidInputException;
import com.naveen.stayease.exception.ValidationException;
import com.naveen.stayease.repository.BookingRepository;
import com.naveen.stayease.repository.RoomRepository;
import com.naveen.stayease.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBookRoom_Success() {
        User user = new User();
        Room room = new Room();
        room.setId(1L);
        room.setTotalNumberOfRooms(5);
        room.setHotel(new Hotel());
        room.getHotel().setId(1L);
        room.setBookings(new ArrayList<>());
        BookingRequest bookingRequest = new BookingRequest(1L, LocalDate.now().plusDays(1));

        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingDetails bookingDetails = bookingService.bookRoom(user, bookingRequest);

        assertNotNull(bookingDetails);
        assertEquals(1L, bookingDetails.getRoomId());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testBookRoom_RoomNotFound() {
        BookingRequest bookingRequest = new BookingRequest(1L, LocalDate.now().plusDays(1));

        when(roomRepository.findById(anyLong())).thenReturn(Optional.empty());

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            bookingService.bookRoom(new User(), bookingRequest);
        });

        assertEquals("Room doesn't exist with roomId 1", exception.getMessage());
    }

    @Test
    void testBookRoom_InvalidBookingDate() {
        BookingRequest bookingRequest = new BookingRequest(1L, LocalDate.now().minusDays(1));

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            bookingService.bookRoom(new User(), bookingRequest);
        });

        assertEquals("Booking date is not valid.", exception.getMessage());
    }

    @Test
    void testBookRoom_RoomNotAvailable() {
        Room room = new Room();
        room.setId(1L);
        room.setBookings(new ArrayList<>());
        BookingRequest bookingRequest = new BookingRequest(1L, LocalDate.now().plusDays(1));

        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            bookingService.bookRoom(new User(), bookingRequest);
        });

        assertEquals("The requested room is not available on the requested date. Please choose any available rooms.", exception.getMessage());
    }

    @Test
    void testGetAllBookings_Success() {
        User user = new User();
        Booking booking = new Booking();
        booking.setBookingId(UUID.randomUUID());
        booking.setRoom(new Room());
        booking.getRoom().setHotel(new Hotel());
        booking.getRoom().setId(1L);
        booking.getRoom().setName("Deluxe");
        booking.getRoom().getHotel().setId(1L);
        booking.getRoom().getHotel().setName("LiveWell Plaza");
        booking.setBookingDate(LocalDate.now().plusDays(1));
        user.setBookings(Collections.singletonList(booking));

        List<BookingDetails> bookings = bookingService.getAllBookings(user);

        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());
        assertEquals(1, bookings.size());
    }

    @Test
    void testGetAllBookings_NoBookings() {
        User user = new User();
        user.setBookings(Collections.emptyList());

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            bookingService.getAllBookings(user);
        });

        assertEquals("You do not have any bookings yet", exception.getMessage());
    }

    @Test
    void testCancelBooking_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        Booking booking = new Booking();
        booking.setBookingId(UUID.randomUUID());
        booking.setUser(user);
        booking.setBookingDate(LocalDate.now().plusDays(1));

        when(bookingRepository.findById(any(UUID.class))).thenReturn(Optional.of(booking));

        bookingService.cancelBooking(user, booking.getBookingId());

        verify(bookingRepository, times(1)).delete(any(Booking.class));
    }

    @Test
    void testCancelBooking_BookingNotFound() {
        when(bookingRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            bookingService.cancelBooking(new User(), UUID.randomUUID());
        });

        assertEquals("Booking Id is invalid", exception.getMessage());
    }

    @Test
    void testCancelBooking_BookingFulfilled() {
        User user = new User();
        user.setEmail("test@example.com");
        Booking booking = new Booking();
        booking.setBookingId(UUID.randomUUID());
        booking.setUser(user);
        booking.setBookingDate(LocalDate.now().minusDays(1));

        when(bookingRepository.findById(any(UUID.class))).thenReturn(Optional.of(booking));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            bookingService.cancelBooking(user, booking.getBookingId());
        });

        assertEquals("This booking is fulfilled", exception.getMessage());
    }
}
