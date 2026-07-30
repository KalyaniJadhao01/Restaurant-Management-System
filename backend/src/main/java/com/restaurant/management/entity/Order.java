package com.restaurant.management.entity;


import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;



@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",
            nullable = false)
    private Customer customer;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id",
            nullable = false)
    private RestaurantTable table;



    @Column(nullable = false)
    private BigDecimal totalAmount;



    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;



    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> items;



    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;



    private LocalDateTime updatedAt;




    @PrePersist
    protected void onCreate(){

        createdAt = LocalDateTime.now();

        updatedAt = LocalDateTime.now();


        if(status == null){

            status = OrderStatus.PENDING;

        }

    }




    @PreUpdate
    protected void onUpdate(){

        updatedAt = LocalDateTime.now();

    }

}
