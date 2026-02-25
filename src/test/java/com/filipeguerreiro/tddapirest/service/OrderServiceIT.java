package com.filipeguerreiro.tddapirest.service;

import com.filipeguerreiro.tddapirest.model.Product;
import com.filipeguerreiro.tddapirest.mongodbcontainer.AbstractIntegrationTest;
import com.filipeguerreiro.tddapirest.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderServiceIT extends AbstractIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldDecreaseStockWhenOrderIsCreated() {

        // Arrange
        Product product = new Product("SKU-ORDER-1", "Monitor", 1000.0);
        product.setStock(10);
        product = productRepository.save(product);

        // Act
        orderService.createOrder("SKU-ORDER-1", 3);

        // Assert
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getStock()).isEqualTo(7);
    }

    @Test
    void shouldRollBackWhenStockIsInsufficient() {

        // Arrange
        Product product = new Product("SKU-ORDER-2", "Mouse", 100.0);
        product.setStock(5);
        productRepository.save(product);

        // Act & Assert: Tenta comprar 10 unidades e espera uma exceção
        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder("SKU-ORDER-2", 10));

        // Assert: Garante que a transação sofreu rollback e o estoque continua sendo 5
        Product unchangedProduct = productRepository.findBySku("SKU-ORDER-2").orElseThrow();
        assertThat(unchangedProduct.getStock()).isEqualTo(5);
    }

}
