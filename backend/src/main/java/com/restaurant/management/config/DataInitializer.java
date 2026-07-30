package com.restaurant.management.config;

import com.restaurant.management.entity.auth.Role;
import com.restaurant.management.repository.auth.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {

        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("MANAGER");
        createRoleIfNotExists("WAITER");
        createRoleIfNotExists("CASHIER");
    }

    private void createRoleIfNotExists(String roleName) {

        if (roleRepository.findByName(roleName).isEmpty()) {

            Role role = new Role();
            role.setName(roleName);

            roleRepository.save(role);

            System.out.println("Created Role : " + roleName);
        }
    }
}