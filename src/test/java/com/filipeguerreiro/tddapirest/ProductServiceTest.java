package com.filipeguerreiro.tddapirest;

import com.filipeguerreiro.tddapirest.model.Product;
import com.filipeguerreiro.tddapirest.repository.ProductRepository;
import com.filipeguerreiro.tddapirest.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        // Arrange
        Product product = new Product("SKU-123", "Teclado", -50.00);

        // Act && Assert

        assertThrows(IllegalArgumentException.class, () -> productService.create(product));

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldCreateProductSuccessfully() {

        // Arrange
        Product product = new Product("SKU-123", "Teclado", 200.0);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        // Act
        Product createdProduct = productService.create(product);

        // Assert
        assertNotNull(createdProduct);
        assertEquals(product.getSku(), createdProduct.getSku());
        assertEquals(product.getTitle(), createdProduct.getTitle());
        assertEquals(product.getPrice(), createdProduct.getPrice());

        verify(productRepository).save(any(Product.class));

    }
}
