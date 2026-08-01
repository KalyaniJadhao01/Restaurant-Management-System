package com.restaurant.management.config;


import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;



@Configuration
public class CorsConfig {


    @Bean
    public CorsConfigurationSource corsConfigurationSource(){


        CorsConfiguration configuration =
                new CorsConfiguration();



        configuration.setAllowedOrigins(

                List.of(

                        "http://127.0.0.1:3000",
                        "http://localhost:3000",

                        "http://127.0.0.1:5500",
                        "http://localhost:5500",
                        "https://6a6e345f8a2a1e0fb3e0737d--grand-sunshine-8b08a3.netlify.app"


                )

        );



        configuration.setAllowedMethods(

                List.of(

                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH",
                        "OPTIONS"

                )

        );



        configuration.setAllowedHeaders(

                List.of("*")

        );



        configuration.setAllowCredentials(true);



        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();



        source.registerCorsConfiguration(
                "/**",
                configuration
        );



        return source;

    }

}