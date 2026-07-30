package com.restaurant.management.controller.auth;

import com.restaurant.management.dto.auth.RegisterUserRequest;
import com.restaurant.management.dto.common.ApiResponse;
import com.restaurant.management.service.auth.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import com.restaurant.management.dto.auth.LoginRequest;
import com.restaurant.management.dto.auth.LoginResponse;
import com.restaurant.management.entity.auth.User;
import com.restaurant.management.repository.auth.UserRepository;
import com.restaurant.management.security.JwtService;
import com.restaurant.management.service.auth.UserService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

//    public AuthController(UserService userService) {
//        this.userService = userService;
//    }

    public AuthController(
            UserService userService,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository
    ){

        this.userService=userService;
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
        this.userRepository=userRepository;
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(
            @Valid @RequestBody RegisterUserRequest request) {

        String message = userService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, message, null));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request
    ){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );


        User user=userRepository.findByEmail(request.getEmail())
                .orElseThrow();


        String token=jwtService.generateToken(
                user.getEmail(),
                user.getRole().getName()
        );


        return ResponseEntity.ok(
                new LoginResponse(
                        token,
                        user.getRole().getName()
                )
        );

    }
}