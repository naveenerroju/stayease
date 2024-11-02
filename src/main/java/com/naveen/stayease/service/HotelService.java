package com.naveen.stayease.service;

import com.naveen.stayease.dto.AddHotelRequest;
import com.naveen.stayease.entity.Hotel;
import com.naveen.stayease.exception.InvalidInputException;
import com.naveen.stayease.repository.HotelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService implements IHotelService{

    @Autowired
    private HotelRepository repository;
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

    @Override
    public List<Hotel> getAvailableHotels() {
        return repository.findByAvailability(true);
    }
}