package com.restaurant.management.service.auth;

import com.restaurant.management.dto.auth.RegisterUserRequest;

public interface UserService {

    String register(RegisterUserRequest request);

}