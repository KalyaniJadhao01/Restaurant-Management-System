package com.restaurant.management.entity;


import jakarta.persistence.*;

import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;



@Entity
@Table(name = "bills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;




    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "order_id",
            nullable = false,
            unique = true
    )
    private Order order;





    @Column(nullable = false)
    private BigDecimal subtotal;





    @Column(nullable = false)
    private BigDecimal taxAmount;





    @Column(nullable = false)
    private BigDecimal discountAmount;





    @Column(nullable = false)
    private BigDecimal totalAmount;





    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillStatus status;





    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;




    private LocalDateTime updatedAt;






    @PrePersist
    protected void onCreate(){


        createdAt = LocalDateTime.now();

        updatedAt = LocalDateTime.now();


        if(status == null){

            status = BillStatus.UNPAID;

        }


        if(discountAmount == null){

            discountAmount = BigDecimal.ZERO;

        }

    }






    @PreUpdate
    protected void onUpdate(){

        updatedAt = LocalDateTime.now();

    }


}
