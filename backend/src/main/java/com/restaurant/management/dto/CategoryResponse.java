package com.restaurant.management.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {


    private Long id;


    private String name;


    private Boolean active;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;

}