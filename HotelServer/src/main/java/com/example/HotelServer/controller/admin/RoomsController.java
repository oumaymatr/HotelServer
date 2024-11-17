package com.example.HotelServer.controller.admin;

import com.example.HotelServer.dto.RoomDto;
import com.example.HotelServer.services.admin.rooms.RoomsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class RoomsController {
  private final RoomsService roomService;
  @PostMapping("/room")
  private ResponseEntity<?> postRoom(@RequestBody RoomDto roomDto){
    boolean success = roomService.postRoom(roomDto);
    if (success){
      return ResponseEntity.status(HttpStatus.OK).build();
    }else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }
}
