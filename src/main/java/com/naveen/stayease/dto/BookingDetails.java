package com.naveen.stayease.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetails {
    private UUID bookingId;
    private long hotelId;
    private String hotelName;
    private long roomId;
    private String roomName;
    private LocalDate bookingDate;
}
