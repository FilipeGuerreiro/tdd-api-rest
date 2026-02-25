package com.filipeguerreiro.tddapirest.repository;

import com.filipeguerreiro.tddapirest.model.Attribute;
import com.filipeguerreiro.tddapirest.model.Product;
import com.filipeguerreiro.tddapirest.model.dto.CategoryStockDTO;
import com.filipeguerreiro.tddapirest.mongodbcontainer.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

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

    @Test
    void shouldAggregateTotalStockByCategory() {

        // Arrange: Cria produtos em categorias diferentes
        Product p1 = new Product("SKU-AGG-1", "TV", 100.0);
        p1.setCategory("Eletrônicos");
        p1.setStock(10);

        Product p2 = new Product("SKU-AGG-2", "Rádio", 50.0);
        p2.setCategory("Eletrônicos");
        p2.setStock(15);

        Product p3 = new Product("SKU-AGG-3", "Mesa", 150.0);
        p3.setCategory("Móveis");
        p3.setStock(5);

        productRepository.saveAll(List.of(p1, p2, p3));

        // Act: Execute a agregação no banco
        List<CategoryStockDTO> result = productRepository.aggregateTotalStockByCategory();

        // Assert: Verifica se agrupou e somou corretamente
        assertThat(result)
                .hasSize(2)
                .anyMatch(r -> r.id().equals("Eletrônicos") && r.totalStock() == 25)
                .anyMatch(r -> r.id().equals("Móveis") && r.totalStock() == 5);

    }

    @Test
    void shouldSaveProductWithEmbeddedAttributes() {
        // Arrange
        Product product = new Product("SKU-EMBEDDED-1", "Smartphone", 100.0);

        product.setAttributes(
                List.of(
                        new Attribute("Cor", "Preto"),
                        new Attribute("Memória", "256GB")
                )
        );

        // Act
        Product savedProduct = productRepository.save(product);

        // Assert
        assertThat(savedProduct.getAttributes()).hasSize(2);

        assertThat(savedProduct.getAttributes().get(0).name()).isEqualTo("Cor");
        assertThat(savedProduct.getAttributes().get(0).value()).isEqualTo("Preto");


        assertThat(savedProduct.getAttributes().get(1).name()).isEqualTo("Memória");
        assertThat(savedProduct.getAttributes().get(1).value()).isEqualTo("256GB");

    }
}
