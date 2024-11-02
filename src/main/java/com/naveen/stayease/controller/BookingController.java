package com.naveen.stayease.controller;

import com.naveen.stayease.dto.BookingRequest;
import com.naveen.stayease.entity.Booking;
import com.naveen.stayease.entity.User;
import com.naveen.stayease.service.BookingService;
import com.naveen.stayease.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
@Validated
public class BookingController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @PostMapping()
    public ResponseEntity<Booking> bookRoom(@RequestHeader("Authorization") String token, @RequestBody BookingRequest bookingRequest){
        // Remove "Bearer " prefix
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        User user = userService.getUserUsingToken(token);
        Booking booking = bookingService.bookRoom(user, bookingRequest);

        return ResponseEntity.ok(booking);
    }
}
