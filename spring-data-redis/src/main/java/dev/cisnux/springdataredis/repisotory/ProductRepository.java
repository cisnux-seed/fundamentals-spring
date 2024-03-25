package dev.cisnux.springdataredis.repisotory;

import dev.cisnux.springdataredis.entity.Product;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends KeyValueRepository<Product, String> {
}
