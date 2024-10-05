package com.naveen.stayease.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * The table name is pluralised because the name 'ROOM' may throw errors in some databases.
 * User could be a keyword in some databases.
 */
@Entity
@Setter
@Getter
@Table(name = "ROOMS")
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Room name cannot be empty")
    private String name;

    @NotEmpty(message = "description cannot be empty")
    private String description;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

}
