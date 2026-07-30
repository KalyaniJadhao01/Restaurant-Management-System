package com.restaurant.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI restaurantAPI() {

        return new OpenAPI()

                .info(

                        new Info()

                                .title("Restaurant Management System API")

                                .version("1.0.0")

                                .description(
                                        "REST APIs for Restaurant Management System built with Spring Boot."
                                )


                );

    }

}