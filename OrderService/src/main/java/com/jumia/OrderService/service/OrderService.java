package com.jumia.OrderService.service;

import com.jumia.OrderService.model.OrderRequest;
import com.jumia.OrderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
