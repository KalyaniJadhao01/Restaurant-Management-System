package com.restaurant.management.controller;


import com.restaurant.management.dto.TableRequest;
import com.restaurant.management.dto.TableResponse;

import com.restaurant.management.entity.TableStatus;

import com.restaurant.management.service.TableService;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;


import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;



@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {



    private final TableService tableService;



    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping
    public ResponseEntity<TableResponse> createTable(
            @Valid @RequestBody TableRequest request
    ){


        return new ResponseEntity<>(

                tableService.createTable(request),

                HttpStatus.CREATED

        );

    }




    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','WAITER')")
    @GetMapping
    public ResponseEntity<Page<TableResponse>> getAllTables(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(

                tableService.getAllTables(
                        page,
                        size,
                        sortBy,
                        direction
                )

        );

    }




    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','WAITER')")
    @GetMapping("/{id}")
    public ResponseEntity<TableResponse> getTableById(
            @PathVariable Long id
    ){


        return ResponseEntity.ok(
                tableService.getTableById(id)
        );

    }




    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<TableResponse> updateTable(
            @PathVariable Long id,

            @Valid @RequestBody TableRequest request
    ){


        return ResponseEntity.ok(
                tableService.updateTable(id, request)
        );

    }




    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTable(
            @PathVariable Long id
    ){


        tableService.deleteTable(id);


        return ResponseEntity.ok(
                "Table deleted successfully"
        );

    }




    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','WAITER')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<TableResponse> updateStatus(
            @PathVariable Long id,

            @RequestParam TableStatus status
    ){


        return ResponseEntity.ok(

                tableService.updateTableStatus(
                        id,
                        status
                )

        );

    }




    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','WAITER')")
    @GetMapping("/available")
    public ResponseEntity<List<TableResponse>> getAvailableTables(){


        return ResponseEntity.ok(
                tableService.getAvailableTables()
        );

    }


}