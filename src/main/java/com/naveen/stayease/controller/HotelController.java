package com.naveen.stayease.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotel")
public class HotelController {

    @PostMapping("/add")
    public String addHotel(@RequestBody String string){
        return "Hotel Added";
    }
}
