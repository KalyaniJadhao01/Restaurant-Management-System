package com.restaurant.management.config;

import com.restaurant.management.entity.auth.Role;
import com.restaurant.management.entity.auth.User;
import com.restaurant.management.repository.auth.RoleRepository;
import com.restaurant.management.repository.auth.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("MANAGER");
        createRoleIfNotExists("WAITER");
        createRoleIfNotExists("CASHIER");
        createRoleIfNotExists("CUSTOMER");
        createRoleIfNotExists("CHEF");

        createDefaultAdmin();
    }

    private void createRoleIfNotExists(String roleName) {

        if (roleRepository.findByName(roleName).isEmpty()) {

            Role role = new Role();
            role.setName(roleName);

            roleRepository.save(role);

            System.out.println("Created Role : " + roleName);
        }
    }

    private void createDefaultAdmin() {

        if (userRepository.findByEmail("admin@restaurant.com").isPresent()) {
            return;
        }

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

        User admin = new User();
        admin.setFullName("System Administrator");
        admin.setEmail("admin@restaurant.com");
        admin.setPhone("9999999999");
        admin.setPassword(passwordEncoder.encode("Admin@123"));
        admin.setEnabled(true);
        admin.setRole(adminRole);

        userRepository.save(admin);

        System.out.println("Default Admin Created");
    }
}