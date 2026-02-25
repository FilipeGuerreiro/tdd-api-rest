package com.filipeguerreiro.tddapirest;

import com.filipeguerreiro.tddapirest.controller.ProductController;
import com.filipeguerreiro.tddapirest.model.Product;
import com.filipeguerreiro.tddapirest.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}
