package com.naveen.stayease.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddRoomRequest {
    @NotBlank(message = "room name cannot be empty")
    private String name;
    @NotBlank(message = "room description cannot be empty")
    private String description;
    @NotNull(message = "room hotel id cannot be empty")
    private long hotel;
    @NotNull(message = "total number of room cannot be empty")
    @Min(value = 1, message = "at least 1 room should be there of this type")
    private int totalNumberOfRooms;
}
