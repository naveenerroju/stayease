package com.naveen.stayease.service;

import com.naveen.stayease.dto.AddRoomRequest;
import com.naveen.stayease.dto.HotelRoomAvailabilityDTO;
import com.naveen.stayease.entity.Room;

import java.time.LocalDate;
import java.util.List;

public interface IRoomService {
    Room addRoom(AddRoomRequest roomRequest);
    List<Room> getAllRooms();
    List<HotelRoomAvailabilityDTO> getAvailableRooms(LocalDate searchDate);
}
