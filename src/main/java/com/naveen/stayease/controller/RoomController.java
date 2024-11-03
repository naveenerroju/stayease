package com.naveen.stayease.controller;

import com.naveen.stayease.dto.AddRoomRequest;
import com.naveen.stayease.dto.HotelRoomAvailabilityDTO;
import com.naveen.stayease.entity.Room;
import com.naveen.stayease.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/room")
@Validated
public class RoomController {

    @Autowired
    private RoomService service;

    @PostMapping()
    public ResponseEntity<Room> addRoom(@Valid @RequestBody AddRoomRequest roomRequest){
        Room room = service.addRoom(roomRequest);
        return ResponseEntity.ok(room);
    }

    @GetMapping()
    public ResponseEntity<List<Room>> getRooms(){
        return ResponseEntity.ok(service.getAllRooms());
    }

    /**
     * example url baser_url/available-rooms?date=2024-11-05
     * @param date 2024-11-05
     * @return
     */
    @GetMapping("/available-rooms")
    public List<HotelRoomAvailabilityDTO> getAvailableRooms(@RequestParam("date") LocalDate date) {
        return service.getAvailableRooms(date);
    }

}
