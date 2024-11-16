package com.example.HotelServer.services.auth;

import com.example.HotelServer.dto.SignupRequest;
import com.example.HotelServer.dto.UserDto;

public interface AuthService {
    UserDto createUser (SignupRequest signupRequest);
}
