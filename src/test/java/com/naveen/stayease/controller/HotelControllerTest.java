package com.naveen.stayease.controller;

import com.naveen.stayease.dto.AddHotelRequest;
import com.naveen.stayease.entity.Hotel;
import com.naveen.stayease.service.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HotelControllerTest {

    @Mock
    private HotelService hotelService;

    @InjectMocks
    private HotelController hotelController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(hotelController).build();
    }

    @Test
    public void testAddHotel() throws Exception {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hotel Name");

        when(hotelService.addHotel(any(AddHotelRequest.class))).thenReturn(hotel);

        mockMvc.perform(post("/hotel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Hotel Name\",\"location\":[12.34,56.78],\"description\":\"Description\",\"availability\":true}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Hotel Name"));
    }

    @Test
    public void testGetAllHotels() throws Exception {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hotel Name");

        when(hotelService.getHotels()).thenReturn(Collections.singletonList(hotel));

        mockMvc.perform(get("/hotel"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Hotel Name"));
    }

    @Test
    public void testGetHotelById() throws Exception {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hotel Name");

        when(hotelService.getHotels(1L)).thenReturn(hotel);

        mockMvc.perform(get("/hotel/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Hotel Name"));
    }
}
