package com.filipeguerreiro.tddapirest.service;

import com.filipeguerreiro.tddapirest.model.Order;
import com.filipeguerreiro.tddapirest.model.Product;
import com.filipeguerreiro.tddapirest.repository.OrderRepository;
import com.filipeguerreiro.tddapirest.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void createOrder(String sku, Integer quantity) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        if (quantity > product.getStock()) {
            throw new IllegalArgumentException("Estoque insuficiente");
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        Order order = new Order(sku, quantity, product.getPrice() * quantity);
        orderRepository.save(order);
    }
}
