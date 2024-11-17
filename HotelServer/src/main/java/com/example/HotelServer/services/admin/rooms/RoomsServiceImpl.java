package com.example.HotelServer.services.admin.rooms;

import com.example.HotelServer.dto.RoomDto;
import com.example.HotelServer.dto.RoomsResponseDto;
import com.example.HotelServer.entity.Room;
import com.example.HotelServer.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j // Add this Lombok annotation for logging
public class RoomsServiceImpl implements RoomsService {
  private final RoomRepository roomRepository;

  public RoomsServiceImpl(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  @Override
  public RoomsResponseDto getAllRooms(int pageNumber) {
    log.info("Fetching rooms for page: {}", pageNumber);
    try {
      Pageable pageable = PageRequest.of(pageNumber, 6);
      Page<Room> roomPage = roomRepository.findAll(pageable);

      RoomsResponseDto roomsResponseDto = new RoomsResponseDto();
      roomsResponseDto.setPagesNumber(roomPage.getPageable().getPageNumber());
      roomsResponseDto.setTotalPages(roomPage.getTotalPages());
      roomsResponseDto.setRoomDtoList(roomPage.stream()
        .map(Room::getRoomDto)
        .collect(Collectors.toList()));

      log.info("Successfully retrieved {} rooms", roomPage.getContent().size());
      return roomsResponseDto;
    } catch (Exception e) {
      log.error("Error fetching rooms: ", e);
      throw e;
    }
  }

  @Override
  public boolean postRoom(RoomDto roomDto) {
    log.info("Attempting to create new room: {}", roomDto.getName());
    try {
      Room room = new Room();
      room.setName(roomDto.getName());
      room.setType(roomDto.getType());
      room.setPrice(roomDto.getPrice());
      room.setAvailable(true);
      roomRepository.save(room);
      log.info("Successfully created room with name: {}", room.getName());
      return true;
    } catch (Exception e) {
      log.error("Error creating room: ", e);
      return false;
    }
  }
}
