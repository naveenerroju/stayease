package com.naveen.stayease.controller;

import com.naveen.stayease.dto.BookingDetails;
import com.naveen.stayease.dto.BookingRequest;
import com.naveen.stayease.entity.Booking;
import com.naveen.stayease.entity.User;
import com.naveen.stayease.service.BookingService;
import com.naveen.stayease.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/book")
@Validated
public class BookingController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @PostMapping()
    public ResponseEntity<BookingDetails> bookRoom(@RequestHeader("Authorization") String token, @Valid @RequestBody BookingRequest bookingRequest){
        // Remove "Bearer " prefix
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        User user = userService.getUserUsingToken(token);
        BookingDetails booking = bookingService.bookRoom(user, bookingRequest);

        return ResponseEntity.ok(booking);
    }

    @GetMapping()
    public ResponseEntity<List<BookingDetails>> getBookings(@RequestHeader("Authorization") String token){
        // Remove "Bearer " prefix
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        User user = userService.getUserUsingToken(token);
        return ResponseEntity.ok(bookingService.getAllBookings(user));
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable("bookingId") UUID bookingId, @RequestHeader("Authorization") String token){
        // Remove "Bearer " prefix
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        User user = userService.getUserUsingToken(token);
        bookingService.cancelBooking(user, bookingId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
