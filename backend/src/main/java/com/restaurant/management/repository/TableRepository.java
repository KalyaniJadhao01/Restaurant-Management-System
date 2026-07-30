package com.restaurant.management.repository;


import com.restaurant.management.entity.RestaurantTable;
import com.restaurant.management.entity.TableStatus;


import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;



public interface TableRepository
        extends JpaRepository<RestaurantTable,Long> {



    Optional<RestaurantTable>
    findByTableNumber(String tableNumber);



    List<RestaurantTable>
    findByStatus(TableStatus status);


}