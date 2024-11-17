package com.example.HotelServer.repository;

import com.example.HotelServer.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
