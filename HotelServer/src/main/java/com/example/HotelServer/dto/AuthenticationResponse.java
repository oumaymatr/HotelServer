package com.example.HotelServer.dto;

import com.example.HotelServer.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {
  private String jwt;
  private Long userId;
  private UserRole userRole;
}
