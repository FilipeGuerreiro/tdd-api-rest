package com.filipeguerreiro.tddapirest.controller;

import com.filipeguerreiro.tddapirest.model.Product;
import com.filipeguerreiro.tddapirest.mongodbcontainer.AbstractIntegrationTest;
import com.filipeguerreiro.tddapirest.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class ProductControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void shouldReturnAllProductsFromDatabase() throws Exception {
        // Arrange
        productRepository.saveAll(List.of(
                new Product("SKU-INT-1", "Teclado Mecânico", 250.0),
                new Product("SKU-INT-2", "Mouse Gamer", 150.0)
        ));

        // Act & Assert
        mockMvc.perform(get("/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].sku").value("SKU-INT-1"))
                .andExpect(jsonPath("$[0].title").value("Teclado Mecânico"))
                .andExpect(jsonPath("$[1].sku").value("SKU-INT-2"))
                .andExpect(jsonPath("$[1].title").value("Mouse Gamer"));
    }
}
