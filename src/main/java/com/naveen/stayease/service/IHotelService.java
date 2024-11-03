package com.naveen.stayease.service;

import com.naveen.stayease.dto.AddHotelRequest;
import com.naveen.stayease.dto.HotelRoomAvailabilityDTO;
import com.naveen.stayease.entity.Hotel;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IHotelService {
    List<Hotel> getHotels();
    Hotel getHotels(long id);
    Hotel addHotel(AddHotelRequest hotel);
    List<Hotel> getAvailableHotels();
}
