package com.naveen.stayease.service;

import com.naveen.stayease.dto.AddHotelRequest;
import com.naveen.stayease.entity.Hotel;

import java.util.List;

public interface IHotelService {
    List<Hotel> getHotels();
    Hotel getHotels(long id);
    Hotel addHotel(AddHotelRequest hotel);
    List<Hotel> getAvailableHotels();
}
