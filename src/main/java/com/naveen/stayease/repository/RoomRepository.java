package com.naveen.stayease.repository;

import com.naveen.stayease.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("""
    SELECT r
    FROM Room r
    LEFT JOIN r.bookings b ON b.bookingDate = :searchDate
    WHERE r.totalNumberOfRooms >
          (SELECT COUNT(bk) FROM Booking bk WHERE bk.room.id = r.id AND bk.bookingDate = :searchDate)
    """)
    List<Room> findAvailableRooms(@Param("searchDate") LocalDate searchDate);
}
