package com.restaurant.management.service;


import com.restaurant.management.dto.BillResponse;
import com.restaurant.management.entity.BillStatus;


import java.math.BigDecimal;
import java.util.List;


public interface BillService {


    BillResponse generateBill(Long orderId,
                              BigDecimal discountAmount);


    BillResponse getBillById(Long id);


    BillResponse getBillByOrderId(Long orderId);


    List<BillResponse> getAllBills();


    BillResponse updateBillStatus(Long id,
                                  BillStatus status);


}