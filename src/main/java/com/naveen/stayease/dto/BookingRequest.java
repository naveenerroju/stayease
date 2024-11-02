package com.naveen.stayease.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    @NotNull(message = "Room Id must be provided")
    @Min(value = 1, message = "roomId must be valid")
    private long roomId;
    @NotNull(message = "Date for booking must be provided")
    private LocalDate bookingDate;
}
