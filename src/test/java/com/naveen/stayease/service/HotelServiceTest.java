package com.naveen.stayease.service;

import com.naveen.stayease.dto.AddHotelRequest;
import com.naveen.stayease.entity.Hotel;
import com.naveen.stayease.exception.InvalidInputException;
import com.naveen.stayease.repository.HotelRepository;
import com.naveen.stayease.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private HotelService hotelService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        hotelService = new HotelService(hotelRepository, roomRepository);
    }

    @Test
    void testGetHotels_Success() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");

        when(hotelRepository.findById(anyLong())).thenReturn(Optional.of(hotel));

        Hotel result = hotelService.getHotels(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Hotel", result.getName());
    }

    @Test
    void testGetHotels_NotFound() {
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.empty());

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            hotelService.getHotels(1L);
        });

        assertEquals("provided ID doesn't exist in database", exception.getMessage());
    }

    @Test
    void testAddHotel_Success() {
        AddHotelRequest hotelRequest = new AddHotelRequest("Test Hotel", new Double[]{12.34, 56.78}, "Description", true);
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");

        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        Hotel result = hotelService.addHotel(hotelRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Hotel", result.getName());
    }

    @Test
    void testGetAllHotels_Success() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");

        when(hotelRepository.findAll()).thenReturn(Collections.singletonList(hotel));

        List<Hotel> result = hotelService.getHotels();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Hotel", result.get(0).getName());
    }
}
