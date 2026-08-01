package com.restaurant.management.security;


import com.restaurant.management.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;



@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;



    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService
    ) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;

    }





    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {



        String path = request.getServletPath();



        // Skip authentication for public endpoints

        if(path.startsWith("/api/auth")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || request.getMethod().equalsIgnoreCase("OPTIONS")){


            filterChain.doFilter(
                    request,
                    response
            );

            return;

        }





        String authHeader = request.getHeader("Authorization");



        // No token -> continue without authentication

        if(authHeader == null ||
                !authHeader.startsWith("Bearer ")) {


            filterChain.doFilter(
                    request,
                    response
            );

            return;

        }





        try {


            String token =
                    authHeader.substring(7);



            String username =
                    jwtService.extractUsername(token);




            if(username != null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {



                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(username);




                if(jwtService.validateToken(token)) {


                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(

                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()

                            );



                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);

                }

            }


        } catch(Exception e){

            System.out.println(
                    "JWT Error: " + e.getMessage()
            );

        }




        filterChain.doFilter(
                request,
                response
        );


    }

}