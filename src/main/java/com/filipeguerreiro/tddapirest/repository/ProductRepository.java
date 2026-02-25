package com.filipeguerreiro.tddapirest.repository;

import com.filipeguerreiro.tddapirest.model.Product;
import com.filipeguerreiro.tddapirest.model.dto.CategoryStockDTO;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findBySku(String sku);

    @Aggregation(pipeline = {
            "{ $group:  { _id: '$category', totalStock:  { $sum:  '$stock' } } }"
    })
    List<CategoryStockDTO> aggregateTotalStockByCategory();

}
