package com.naveen.stayease.controller;

import com.naveen.stayease.dto.AddHotelRequest;
import com.naveen.stayease.dto.HotelRoomAvailabilityDTO;
import com.naveen.stayease.entity.Hotel;
import com.naveen.stayease.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    /**
     * If availability is not provided, it is by default set to false.
     * @param hotelRequest
     * @return
     */
    @PostMapping()
    public Hotel addHotel(@Valid @RequestBody AddHotelRequest hotelRequest){
        return hotelService.addHotel(hotelRequest);
    }

    @GetMapping()
    public List<Hotel> getAllHotels(){
        return hotelService.getHotels();
    }

    @GetMapping("/{id}")
    public Hotel getHotels(@PathVariable(value = "id") long id){
        return hotelService.getHotels(id);
    }

    /**
     * example url baser_url/available-rooms?date=2024-11-05
     * @param date 2024-11-05
     * @return
     */
    @GetMapping("/available-rooms")
    public List<HotelRoomAvailabilityDTO> getAvailableRooms(@RequestParam("date") LocalDate date) {
        return hotelService.getAvailableRooms(date);
    }

}
