package com.filipeguerreiro.tddapirest.service;

import com.filipeguerreiro.tddapirest.model.Product;
import com.filipeguerreiro.tddapirest.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final ProductRepository productRepository;

    public OrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
    }
}
