package com.restaurant.management.service.impl;

import com.restaurant.management.dto.TableRequest;
import com.restaurant.management.dto.TableResponse;
import com.restaurant.management.entity.RestaurantTable;
import com.restaurant.management.entity.TableStatus;
import com.restaurant.management.exception.ResourceAlreadyExistsException;
import com.restaurant.management.exception.ResourceNotFoundException;
import com.restaurant.management.repository.TableRepository;
import com.restaurant.management.service.TableService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

    private final TableRepository tableRepository;

    @Override
    public TableResponse createTable(TableRequest request) {

        if (tableRepository.findByTableNumber(request.getTableNumber()).isPresent()) {

            throw new ResourceAlreadyExistsException(
                    "Table already exists with number: " + request.getTableNumber()
            );
        }

        RestaurantTable table = RestaurantTable.builder()
                .tableNumber(request.getTableNumber())
                .capacity(request.getCapacity())
                .status(
                        request.getStatus() != null
                                ? request.getStatus()
                                : TableStatus.AVAILABLE
                )
                .build();

        return mapToResponse(tableRepository.save(table));
    }

    @Override
    public TableResponse updateTable(Long id, TableRequest request) {

        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Table not found with id: " + id
                        ));

        table.setTableNumber(request.getTableNumber());

        table.setCapacity(request.getCapacity());

        table.setStatus(
                request.getStatus() != null
                        ? request.getStatus()
                        : TableStatus.AVAILABLE
        );

        return mapToResponse(tableRepository.save(table));
    }

    @Override
    public TableResponse getTableById(Long id) {

        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Table not found with id: " + id
                        ));

        return mapToResponse(table);
    }

    @Override
    public Page<TableResponse> getAllTables(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<RestaurantTable> tablePage = tableRepository.findAll(pageable);

        return tablePage.map(this::mapToResponse);
    }

    @Override
    public List<TableResponse> getAvailableTables() {

        return tableRepository.findByStatus(TableStatus.AVAILABLE)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public TableResponse updateTableStatus(Long id,
                                           TableStatus status) {

        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Table not found with id: " + id
                        ));

        table.setStatus(status);

        return mapToResponse(tableRepository.save(table));
    }

    @Override
    public void deleteTable(Long id) {

        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Table not found with id: " + id
                        ));

        tableRepository.delete(table);
    }

    private TableResponse mapToResponse(RestaurantTable table) {

        return TableResponse.builder()
                .id(table.getId())
                .tableNumber(table.getTableNumber())
                .capacity(table.getCapacity())
                .status(table.getStatus())
                .createdAt(table.getCreatedAt())
                .updatedAt(table.getUpdatedAt())
                .build();
    }

}