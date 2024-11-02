package com.naveen.stayease.controller;

import com.naveen.stayease.dto.AddHotelRequest;
import com.naveen.stayease.entity.Hotel;
import com.naveen.stayease.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Hotel addHotel(@RequestBody AddHotelRequest hotelRequest){
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

    @GetMapping("/available")
    public List<Hotel> getAvailableHotels(){
        return hotelService.getAvailableHotels();
    }

}
