package com.restaurant.management.entity;


import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;



@Entity
@Table(name = "restaurant_tables")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantTable {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(nullable = false, unique = true)
    private String tableNumber;



    @Column(nullable = false)
    private Integer capacity;



    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TableStatus status;



    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;



    private LocalDateTime updatedAt;




    @PrePersist
    protected void onCreate(){

        createdAt = LocalDateTime.now();

        updatedAt = LocalDateTime.now();


        if(status == null){

            status = TableStatus.AVAILABLE;

        }

    }




    @PreUpdate
    protected void onUpdate(){

        updatedAt = LocalDateTime.now();

    }


}