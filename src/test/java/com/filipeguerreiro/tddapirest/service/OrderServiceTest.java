package com.filipeguerreiro.tddapirest.service;

import com.filipeguerreiro.tddapirest.model.Order;
import com.filipeguerreiro.tddapirest.model.Product;
import com.filipeguerreiro.tddapirest.repository.OrderRepository;
import com.filipeguerreiro.tddapirest.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldDecreaseStockAndSaveOrderWhenOrderIsCreated() {
        // Arrange
        String sku = "SKU-TEST";
        Integer quantity = 2;
        Product product = new Product(sku, "Produto teste", 100.0);
        product.setStock(10);

        when(productRepository.findBySku(sku)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        orderService.createOrder(sku, quantity);

        // Assert
        assertThat(product.getStock()).isEqualTo(8);
        verify(productRepository).save(product);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertThat(savedOrder.getSku()).isEqualTo(sku);
        assertThat(savedOrder.getQuantity()).isEqualTo(quantity);
        assertThat(savedOrder.getTotalPrice()).isEqualTo(200.0);
    }
}
