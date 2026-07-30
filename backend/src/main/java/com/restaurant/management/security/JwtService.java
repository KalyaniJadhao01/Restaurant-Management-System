package com.restaurant.management.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;


@Service
public class JwtService {


    private final String SECRET_KEY =
            "restaurantManagementSystemSecretKeyForJWTAuthentication2026";


    public String generateToken(String email,String role){


        return Jwts.builder()
                .subject(email)
                .claim("role",role)
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis()+86400000)
                )
                .signWith(
                        Keys.hmacShaKeyFor(
                                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
                        )
                )
                .compact();
    }



    public String extractUsername(String token){


        return Jwts.parser()
                .verifyWith(
                        Keys.hmacShaKeyFor(
                                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
                        )
                )
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


    public boolean validateToken(String token){

        try{

            Jwts.parser()
                    .verifyWith(
                            Keys.hmacShaKeyFor(
                                    SECRET_KEY.getBytes(StandardCharsets.UTF_8)
                            )
                    )
                    .build()
                    .parseSignedClaims(token);

            return true;

        }
        catch(Exception e){

            return false;
        }
    }

}