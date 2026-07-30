package com.restaurant.management.service;


import com.restaurant.management.dto.TableRequest;
import com.restaurant.management.dto.TableResponse;
import com.restaurant.management.entity.TableStatus;
import org.springframework.data.domain.Page;

import java.util.List;


public interface TableService {


    TableResponse createTable(TableRequest request);


    TableResponse updateTable(Long id,
                              TableRequest request);


    TableResponse getTableById(Long id);


    Page<TableResponse> getAllTables(
            int page,
            int size,
            String sortBy,
            String direction
    );


    List<TableResponse> getAvailableTables();


    TableResponse updateTableStatus(Long id,
                                    TableStatus status);


    void deleteTable(Long id);

}