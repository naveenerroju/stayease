package com.naveen.stayease.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotel")
public class HotelController {

    @PostMapping("/add")
    public String addHotel(@RequestBody String string){
        return "Hotel Added";
    }

    @GetMapping("/allhotels")
    public String getAllHotels(){
        return "all hotels";
    }
}
