package com.filipeguerreiro.tddapirest.controller;

import com.filipeguerreiro.tddapirest.model.Product;
import com.filipeguerreiro.tddapirest.repository.ProductRepository;
import com.filipeguerreiro.tddapirest.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void shouldCreateProductAndReturn201() throws Exception {
        // Arrange
        Product savedProduct = new Product("SKU-123", "Teclado", 150.0);
        savedProduct.setId("65a1b2c3d4e5f6");

        when(productService.create(any(Product.class))).thenReturn(savedProduct);

        String jsonPayload = """
                {
                    "sku": "SKU-123",
                    "title": "Teclado",
                    "price": 150.0
                
                }
                """;

        // Act & Assert

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("65a1b2c3d4e5f6"))
                .andExpect(jsonPath("$.sku").value("SKU-123"));
    }

    @Test
    void shouldReturn400WhenPriceIsNegative() throws Exception {
        // Arrange
        when(productService.create(any(Product.class)))
                .thenThrow(new IllegalArgumentException("O preço não pode ser negativo"));

        String invalidJson = """
                {
                    "sku": "SKU-999",
                    "title": "Produto Invalido",
                    "price": -10.0
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("O preço não pode ser negativo"));
    }

    @Test
    void shouldReturnAllProducts() throws Exception {
        // Arrange
        List<Product> savedProducts = List.of(
                new Product("65a1b2c3d4e5f6", "SKU-123", "Teclado", 150.0),
                new Product("65a1b2c3d4e5f7", "SKU-124", "Mouse", 70.0),
                new Product("65a1b2c3d4e5f8", "SKU-125", "MousePad", 50.0),
                new Product("65a1b2c3d4e5f9", "SKU-126", "Fone", 120.0)

        );
        when(productService.getAll()).thenReturn(savedProducts);

        // Act & Assert
        mockMvc.perform(get("/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("SKU-123"))
                .andExpect(jsonPath("$.length()").value(4));

    }
}
