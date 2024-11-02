package com.naveen.stayease.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddHotelRequest {
    @NotBlank(message = "hotel name cannot be empty")
    private String name;
    @NotNull(message = "location location cannot be empty.")
    @Size(min = 2, max = 2, message = "exactly 2 values are required. 0 is latitude, 1 is longitude")
    private Double[] location;
    @NotBlank(message = "hotel description cannot be empty")
    private String description;
    private boolean availability;
}
