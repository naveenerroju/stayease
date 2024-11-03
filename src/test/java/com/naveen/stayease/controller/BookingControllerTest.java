package com.naveen.stayease.controller;

import com.naveen.stayease.dto.BookingDetails;
import com.naveen.stayease.dto.BookingRequest;
import com.naveen.stayease.entity.User;
import com.naveen.stayease.service.BookingService;
import com.naveen.stayease.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookingControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    void testBookRoom() throws Exception {
        User user = new User();
        BookingDetails bookingDetails = new BookingDetails();
        bookingDetails.setBookingId(UUID.randomUUID());
        bookingDetails.setRoomId(1L);

        when(userService.getUserUsingToken(anyString())).thenReturn(user);
        when(bookingService.bookRoom(any(User.class), any(BookingRequest.class))).thenReturn(bookingDetails);

        mockMvc.perform(post("/book")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomId\":1,\"bookingDate\":\"2024-11-05\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.roomId").value(1L));
    }

    @Test
    void testGetBookings() throws Exception {
        User user = new User();
        BookingDetails bookingDetails = new BookingDetails();
        bookingDetails.setBookingId(UUID.randomUUID());
        bookingDetails.setRoomId(1L);

        when(userService.getUserUsingToken(anyString())).thenReturn(user);
        when(bookingService.getAllBookings(any(User.class))).thenReturn(Collections.singletonList(bookingDetails));

        mockMvc.perform(get("/book")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].roomId").value(1L));
    }

    @Test
    void testCancelBooking() throws Exception {
        User user = new User();
        UUID bookingId = UUID.randomUUID();

        when(userService.getUserUsingToken(anyString())).thenReturn(user);

        mockMvc.perform(delete("/book/{bookingId}", bookingId)
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isNoContent());
    }
}
