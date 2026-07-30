package com.restaurant.management.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "menu_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String name;


    @Column(length = 500)
    private String description;


    @Column(nullable = false)
    private BigDecimal price;


    private String imageUrl;


    @Column(nullable = false)
    private Boolean available;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    private Category category;


    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;



    @PrePersist
    protected void onCreate(){

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if(available == null){
            available = true;
        }

    }



    @PreUpdate
    protected void onUpdate(){

        updatedAt = LocalDateTime.now();

    }

}