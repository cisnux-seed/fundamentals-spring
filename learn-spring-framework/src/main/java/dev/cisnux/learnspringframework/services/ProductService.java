package dev.cisnux.learnspringframework.services;


import dev.cisnux.learnspringframework.repositories.CategoryRepository;
import dev.cisnux.learnspringframework.repositories.ProductRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Scope(value = "prototype")
//@Lazy
@Getter
@Component
public class ProductService {
    private final ProductRepository productRepository;
    @Getter
    private CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductService(ProductRepository productRepository, String name) {
        this.productRepository = productRepository;
    }

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
}
