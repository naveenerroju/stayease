package com.naveen.stayease.service;

import com.naveen.stayease.dto.AddHotelRequest;
import com.naveen.stayease.dto.HotelRoomAvailabilityDTO;
import com.naveen.stayease.entity.Hotel;
import com.naveen.stayease.entity.Room;
import com.naveen.stayease.exception.InvalidInputException;
import com.naveen.stayease.repository.HotelRepository;
import com.naveen.stayease.repository.RoomRepository;
import com.naveen.stayease.util.RoomAvailabilityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HotelService implements IHotelService{

    @Autowired
    private HotelRepository repository;
    @Autowired
    private RoomRepository roomRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<Hotel> getHotels() {
        return repository.findAll();
    }

    @Override
    public Hotel getHotels(long id) {
        Optional<Hotel> hotel = repository.findById(id);
        if(hotel.isPresent()){
            return hotel.get();
        } else {
            throw new InvalidInputException("provided ID doesn't exist in database");
        }
    }

    @Override
    public Hotel addHotel(AddHotelRequest hotelRequest) {
        Hotel hotel = modelMapper.map(hotelRequest, Hotel.class);

        return repository.save(hotel);
    }

}
