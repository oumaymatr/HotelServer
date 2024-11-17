package com.example.HotelServer.services.admin.rooms;

import com.example.HotelServer.dto.RoomDto;

public interface RoomsService {
  boolean postRoom(RoomDto roomDto);
}
