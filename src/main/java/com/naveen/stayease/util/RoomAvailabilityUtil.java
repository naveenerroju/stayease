package com.naveen.stayease.util;

import com.naveen.stayease.entity.Room;

import java.time.LocalDate;

public class RoomAvailabilityUtil {

    private RoomAvailabilityUtil(){}
    /**
     * If the return value is 0, you can consider there is no availability.
     * @param room
     * @param date
     * @return
     */
    public static int numberOfAvailableRooms(Room room, LocalDate date) {

        int bookedCount = (int) room.getBookings().stream()
                .filter(booking -> booking.getBookingDate().equals(date))
                .count();
        return room.getTotalNumberOfRooms() - bookedCount;

    }
}
