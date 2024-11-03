package com.naveen.stayease.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelRoomAvailabilityDTO {
    private Long hotelId;
    private String hotelName;
    private Long roomId;
    private String roomName;
    private int availableRooms;
}

