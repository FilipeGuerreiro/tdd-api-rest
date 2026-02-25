package com.filipeguerreiro.tddapirest.controller;

import com.filipeguerreiro.tddapirest.model.dto.OrderRequest;
import com.filipeguerreiro.tddapirest.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody OrderRequest orderRequest) {
        orderService.createOrder(orderRequest.sku(), orderRequest.quantity());
        return ResponseEntity.ok().build();
    }
}
