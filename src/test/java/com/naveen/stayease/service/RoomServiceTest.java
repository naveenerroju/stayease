package com.naveen.stayease.service;

import com.naveen.stayease.dto.AddRoomRequest;
import com.naveen.stayease.dto.HotelRoomAvailabilityDTO;
import com.naveen.stayease.entity.Booking;
import com.naveen.stayease.entity.Hotel;
import com.naveen.stayease.entity.Room;
import com.naveen.stayease.entity.User;
import com.naveen.stayease.exception.InvalidInputException;
import com.naveen.stayease.repository.HotelRepository;
import com.naveen.stayease.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        roomService = new RoomService(roomRepository, hotelRepository);
    }

    @Test
    void testAddRoom_Success() {
        AddRoomRequest roomRequest = new AddRoomRequest("Room Name", "Description", 5, 1);
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        Room room = new Room();
        room.setId(1L);
        room.setName("Room Name");

        when(hotelRepository.findById(anyLong())).thenReturn(Optional.of(hotel));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Room result = roomService.addRoom(roomRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Room Name", result.getName());
    }

    @Test
    void testAddRoom_HotelNotFound() {
        AddRoomRequest roomRequest = new AddRoomRequest("Room Name", "Description", 5, 1);

        when(hotelRepository.findById(anyLong())).thenReturn(Optional.empty());

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            roomService.addRoom(roomRequest);
        });

        assertEquals("There is no hotel existing with that hotel id", exception.getMessage());
    }

    @Test
    void testGetAllRooms_Success() {
        Room room = new Room();
        room.setId(1L);
        room.setName("Room Name");

        when(roomRepository.findAll()).thenReturn(Collections.singletonList(room));

        List<Room> result = roomService.getAllRooms();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Room Name", result.get(0).getName());
    }

    @Test
    void testGetAvailableRooms_Success() {
        Room room = new Room();
        room.setId(1L);
        room.setName("Room Name");
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hotel Name");
        room.setHotel(hotel);
        room.setTotalNumberOfRooms(3);
        Booking booking = new Booking();
        booking.setUser(new User());
        booking.setRoom(room);
        booking.setBookingDate(LocalDate.of(2045, 1, 1));
        room.setBookings(List.of(booking));

        when(roomRepository.findAvailableRooms(any(LocalDate.class))).thenReturn(Collections.singletonList(room));

        List<HotelRoomAvailabilityDTO> result = roomService.getAvailableRooms(LocalDate.now());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getHotelId());
        assertEquals("Hotel Name", result.get(0).getHotelName());
        assertEquals(1L, result.get(0).getRoomId());
        assertEquals("Room Name", result.get(0).getRoomName());
        assertEquals(3, result.get(0).getAvailableRooms());
    }
}
