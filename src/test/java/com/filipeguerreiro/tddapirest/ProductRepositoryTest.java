package com.filipeguerreiro.tddapirest;

import com.filipeguerreiro.tddapirest.model.Product;
import com.filipeguerreiro.tddapirest.mongodbcontainer.AbstractIntegrationTest;
import com.filipeguerreiro.tddapirest.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldSaveProductSuccessfully() {
        // Arrange
        // Stock Keeping Unit (SKU)
        Product newProduct = new Product();
        newProduct.setSku("SKU-98765");
        newProduct.setTitle("Notebook Genérico");
        newProduct.setPrice(3500.00);

        // Act

        Product savedProduct = productRepository.save(newProduct);

        // Assert

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getSku()).isEqualTo("SKU-98765");

    }

    @Test
    void shouldThrowExceptionWhenSavingDuplicatedSku() {
        // Arrange
        Product product1 = new Product("SKU-UNIQUE","Produto Original", 2000.0);
        productRepository.save(product1);

        Product product2 = new Product("SKU-UNIQUE", "Produto Cópia", 1000.0);
        // Act && Assert

        assertThrows(DuplicateKeyException.class, () -> productRepository.save(product2));
    }
}
