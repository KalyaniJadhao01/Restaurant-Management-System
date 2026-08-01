package com.restaurant.management.service.impl.auth;

import com.restaurant.management.dto.auth.RegisterUserRequest;
import com.restaurant.management.entity.auth.Role;
import com.restaurant.management.entity.auth.User;
import com.restaurant.management.repository.auth.RoleRepository;
import com.restaurant.management.repository.auth.UserRepository;
import com.restaurant.management.service.auth.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.restaurant.management.exception.DuplicateResourceException;
import com.restaurant.management.exception.ResourceNotFoundException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String register(RegisterUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists.");
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("Phone number already exists.");
        }

        Role role = roleRepository.findByName("Customer")
                .orElseThrow(()->
                        new ResourceNotFoundException("Default role CUSTOMER not found"
                        ));
        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        // Password will be encrypted
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(role);

        userRepository.save(user);

        return "User Registered Successfully";
    }
}