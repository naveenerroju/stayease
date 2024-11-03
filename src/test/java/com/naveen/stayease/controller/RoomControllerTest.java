package com.naveen.stayease.controller;

import com.naveen.stayease.dto.AddRoomRequest;
import com.naveen.stayease.dto.HotelRoomAvailabilityDTO;
import com.naveen.stayease.entity.Room;
import com.naveen.stayease.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }

    @Test
    void testAddRoom() throws Exception {
        Room room = new Room();
        room.setId(1L);
        room.setName("Room Name");

        when(roomService.addRoom(any(AddRoomRequest.class))).thenReturn(room);

        mockMvc.perform(post("/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Room Name\",\"description\":\"Description\",\"totalNumberOfRooms\":5,\"hotelId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Room Name"));
    }

    @Test
    void testGetRooms() throws Exception {
        Room room = new Room();
        room.setId(1L);
        room.setName("Room Name");

        when(roomService.getAllRooms()).thenReturn(Collections.singletonList(room));

        mockMvc.perform(get("/room"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Room Name"));
    }

    @Test
    void testGetAvailableRooms() throws Exception {
        HotelRoomAvailabilityDTO dto = new HotelRoomAvailabilityDTO(1L, "Hotel Name", 1L, "Room Name", 3);

        when(roomService.getAvailableRooms(any(LocalDate.class))).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/room/available-rooms")
                        .param("date", "2024-11-05"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].hotelId").value(1L))
                .andExpect(jsonPath("$[0].hotelName").value("Hotel Name"))
                .andExpect(jsonPath("$[0].roomId").value(1L))
                .andExpect(jsonPath("$[0].roomName").value("Room Name"))
                .andExpect(jsonPath("$[0].availableRooms").value(3));
    }
}
