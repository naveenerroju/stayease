package com.naveen.stayease.service;

import com.naveen.stayease.dto.AddRoomRequest;
import com.naveen.stayease.entity.Hotel;
import com.naveen.stayease.entity.Room;
import com.naveen.stayease.exception.InvalidInputException;
import com.naveen.stayease.repository.HotelRepository;
import com.naveen.stayease.repository.RoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService implements IRoomService{
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private HotelRepository hotelRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public Room addRoom(AddRoomRequest roomRequest) {

        Optional<Hotel> hotel = hotelRepository.findById(roomRequest.getHotel());
        if(hotel.isEmpty()){
            throw new InvalidInputException("There is no hotel existing with that hotel id");
        }
        Room room = modelMapper.map(roomRequest, Room.class);
        room.setHotel(hotel.get());

        return roomRepository.save(room);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}
