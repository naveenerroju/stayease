package com.naveen.stayease.service;

import com.naveen.stayease.dto.AddRoomRequest;
import com.naveen.stayease.dto.HotelRoomAvailabilityDTO;
import com.naveen.stayease.entity.Hotel;
import com.naveen.stayease.entity.Room;
import com.naveen.stayease.exception.InvalidInputException;
import com.naveen.stayease.repository.HotelRepository;
import com.naveen.stayease.repository.RoomRepository;
import com.naveen.stayease.util.RoomAvailabilityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService implements IRoomService{
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public RoomService(RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }

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

    @Override
    public List<HotelRoomAvailabilityDTO> getAvailableRooms(LocalDate searchDate) {
        List<Room> availableRooms = roomRepository.findAvailableRooms(searchDate);
        List<HotelRoomAvailabilityDTO> availableRoomDTOs = new ArrayList<>();

        for (Room room : availableRooms) {
            int availableRoomsCount = RoomAvailabilityUtil.numberOfAvailableRooms(room, searchDate);

            availableRoomDTOs.add(new HotelRoomAvailabilityDTO(
                    room.getHotel().getId(),
                    room.getHotel().getName(),
                    room.getId(),
                    room.getName(),
                    availableRoomsCount
            ));
        }
        return availableRoomDTOs;
    }
}
