package com.filipeguerreiro.tddapirest.service;

import com.filipeguerreiro.tddapirest.model.Product;
import com.filipeguerreiro.tddapirest.model.dto.CategoryStockDTO;
import com.filipeguerreiro.tddapirest.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
    void shouldReturnStockByCategory() {
        // Arrange
        List<CategoryStockDTO> expectedResult = List.of(
                new CategoryStockDTO("Eletronicos", 100L),
                new CategoryStockDTO("Livros", 50L)
        );
        when(productRepository.aggregateTotalStockByCategory()).thenReturn(expectedResult);

        // Act
        List<CategoryStockDTO> result = productService.getTotalStockByCategory();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Eletronicos", result.getFirst().id());
        assertEquals(100L, result.getFirst().totalStock());
        verify(productRepository).aggregateTotalStockByCategory();
    }

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
