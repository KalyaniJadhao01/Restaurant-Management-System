package com.restaurant.management.entity;


import jakarta.persistence.*;

import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;



@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "bill_id",
            nullable = false,
            unique = true
    )
    private Bill bill;



    @Column(nullable = false)
    private BigDecimal amount;



    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;



    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;



    @Column(unique = true)
    private String transactionReference;



    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;



    private LocalDateTime updatedAt;





    @PrePersist
    protected void onCreate(){


        createdAt = LocalDateTime.now();

        updatedAt = LocalDateTime.now();



        if(status == null){

            status = PaymentStatus.PENDING;

        }

    }





    @PreUpdate
    protected void onUpdate(){

        updatedAt = LocalDateTime.now();

    }

}