package com.filipeguerreiro.tddapirest.controller;

import com.filipeguerreiro.tddapirest.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Test
    void shouldProcessOrderAndReturn200() throws Exception {
        // Arrange
        String jsonPayload = """
                {
                    "sku": "SKU-ORDER-1",
                    "quantity": 3
                }
                """;

        doNothing().when(orderService).createOrder("SKU-ORDER-1", 3);

        // Act & Assert
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk());

        verify(orderService).createOrder("SKU-ORDER-1", 3);
    }
}
