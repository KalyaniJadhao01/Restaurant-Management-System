package com.restaurant.management.dto;


import com.restaurant.management.entity.TableStatus;

import lombok.*;


import java.time.LocalDateTime;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableResponse {



    private Long id;


    private String tableNumber;


    private Integer capacity;


    private TableStatus status;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;


}