package com.example.HotelServer.services.admin.rooms;

import com.example.HotelServer.dto.RoomDto;
import com.example.HotelServer.entity.Room;
import com.example.HotelServer.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomsServiceImpl implements RoomsService{

  private final RoomRepository roomRepository;

  public RoomsServiceImpl(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }
  @Override
  public boolean postRoom(RoomDto roomDto){
    try{
      Room room = new Room();
      room.setName(roomDto.getName());
      room.setType(roomDto.getType());
      room.setPrice(roomDto.getPrice());
      room.setAvailable(true);
      roomRepository.save(room);
      return true;
    }catch (Exception e){
      return false;
    }
  }
}
