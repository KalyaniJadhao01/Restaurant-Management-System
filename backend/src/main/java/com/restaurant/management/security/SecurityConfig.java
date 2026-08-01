package com.restaurant.management.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {


        http
                .csrf(csrf -> csrf.disable())

                .cors(cors -> {})

                .authorizeHttpRequests(auth -> auth

                        // Allow CORS preflight
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**")
                        .permitAll()


                        // Public APIs
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        )
                        .permitAll()


                        .anyRequest()
                        .authenticated()

                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();

    }




    @Bean
    public CorsConfigurationSource corsConfigurationSource() {


        CorsConfiguration configuration = new CorsConfiguration();


        configuration.setAllowedOrigins(
                List.of(
                        "https://restaurant-management-system-snowy-nu.vercel.app"
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




    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {


        return configuration.getAuthenticationManager();

    }

}