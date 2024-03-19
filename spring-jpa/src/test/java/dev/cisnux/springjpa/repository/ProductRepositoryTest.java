package dev.cisnux.springjpa.repository;

import dev.cisnux.springjpa.entity.Category;
import dev.cisnux.springjpa.entity.Product;
import dev.cisnux.springjpa.model.ProductPrice;
import dev.cisnux.springjpa.model.SimpleProduct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.support.TransactionOperations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionOperations transactionOperations;

    @Test
    void createProduct() {
        Category category = categoryRepository.findById(27L).orElse(null);
        assertNotNull(category);

        Product product = new Product();
        product.setName("Apple iPhone 18 Pro Max");
        product.setPrice(28_000_000L);
        product.setCategory(category);
        productRepository.save(product);

        Product product1 = new Product();
        product1.setName("Apple iPhone 19 Pro Max");
        product1.setPrice(20_000_000L);
        product1.setCategory(category);
        productRepository.save(product1);
    }

    @Test
    void findProducts() {
        List<Product> products = productRepository.findAllByCategory_Name("new gadget");

        assertEquals(2, products.size());
        assertEquals("Apple iPhone 14 Pro Max", products.getFirst().getName());
        assertEquals("Apple iPhone 13 Pro Max", products.get(1).getName());
    }

    @Test
    void findSortedProducts() {
        List<Product> products = productRepository.findAllByCategory_Name("new gadget", Sort.by(Sort.Order.asc("price")));

        assertEquals(2, products.size());
        assertEquals("Apple iPhone 13 Pro Max", products.getFirst().getName());
        assertEquals("Apple iPhone 14 Pro Max", products.get(1).getName());
    }

    @Test
    void findProductsWithPageable() {
        // first page
        // page 0
        var pageable = PageRequest.of(0, 2, Sort.by(Sort.Order.asc("price")));
        var products = productRepository.findAllByCategory_Name("new gadget", pageable);

        assertEquals(2, products.getContent().size());
        assertEquals(0, products.getNumber());
        assertEquals(4, products.getTotalElements());
        assertEquals(2, products.getTotalPages());
        assertEquals("Apple iPhone 13 Pro Max", products.getContent().getFirst().getName());
        assertEquals("Apple iPhone 19 Pro Max", products.getContent().get(1).getName());

        // page 1
        pageable = PageRequest.of(1, 2, Sort.by(Sort.Order.asc("price")));
        products = productRepository.findAllByCategory_Name("new gadget", pageable);

        assertEquals(2, products.getContent().size());
        assertEquals(1, products.getNumber());
        assertEquals(4, products.getTotalElements());
        assertEquals(2, products.getTotalPages());
        assertEquals("Apple iPhone 14 Pro Max", products.getContent().getFirst().getName());
        assertEquals("Apple iPhone 18 Pro Max", products.getContent().get(1).getName());
    }

    @Test
    void testCount() {
        Long count = productRepository.count();
        assertEquals(4, count);

        count = productRepository.countByCategory_Name("new gadget");
        assertEquals(4, count);

        count = productRepository.countByCategory_Name("nothing");
        assertEquals(0, count);
    }

    @Test
    void testExists() {
        boolean exists = productRepository.existsByName("Apple iPhone 14 Pro Max");
        assertTrue(exists);

        exists = productRepository.existsByName("cisnux");
        assertFalse(exists);
    }

    @Test
    void testDelete() {
        transactionOperations.executeWithoutResult(transactionStatus -> { // first transaction
            Category category = categoryRepository.findById(27L).orElse(null);
            assertNotNull(category);

            Product product = new Product();
            product.setName("Samsung Galaxy S20");
            product.setPrice(10_000_000L);
            product.setCategory(category);
            productRepository.save(product); // first transaction

            int delete = productRepository.deleteByName("Samsung Galaxy S20"); // first transaction
            assertEquals(1, delete);

            delete = productRepository.deleteByName("cisnux"); // first transaction
            assertEquals(0, delete);
        });
    }

    @Test
    void testNewDelete() {
        Category category = categoryRepository.findById(27L).orElse(null);
        assertNotNull(category);

        Product product = new Product();
        product.setName("Samsung Galaxy S20");
        product.setPrice(10_000_000L);
        product.setCategory(category);
        productRepository.save(product); // first transaction

        int delete = productRepository.deleteByName("Samsung Galaxy S20"); // second transaction
        assertEquals(1, delete);

        delete = productRepository.deleteByName("cisnux"); // third transaction
        assertEquals(0, delete);
    }

    @Test
    void searchProduct() {
        List<Product> products = productRepository.searchProductUsingName("Apple iPhone 14 Pro Max");

        assertEquals(1, products.size());
        assertEquals("Apple iPhone 14 Pro Max", products.getFirst().getName());
    }

    @Test
    void searchProductWithPageable() {
        Pageable pageable = PageRequest.of(0, 2);
        List<Product> products = productRepository.searchProductUsingName("Apple iPhone 14 Pro Max", pageable);

        assertEquals(1, products.size());
        assertEquals("Apple iPhone 14 Pro Max", products.getFirst().getName());
    }

    @Test
    void searchProductWithQueryAnnotation() {
        // first page
        // page 0
        var pageable = PageRequest.of(0, 2, Sort.by(Sort.Order.asc("price")));
        var products = productRepository.searchProducts("%iPhone%", pageable);

        assertEquals(2, products.getContent().size());
        assertEquals(0, products.getNumber());
        assertEquals(4, products.getTotalElements());
        assertEquals(2, products.getTotalPages());
        assertEquals("Apple iPhone 13 Pro Max", products.getContent().getFirst().getName());
        assertEquals("Apple iPhone 19 Pro Max", products.getContent().get(1).getName());

        // page 1
        pageable = PageRequest.of(1, 2, Sort.by(Sort.Order.asc("price")));
        products = productRepository.searchProducts("%new%", pageable);

        assertEquals(2, products.getContent().size());
        assertEquals(1, products.getNumber());
        assertEquals(4, products.getTotalElements());
        assertEquals(2, products.getTotalPages());
        assertEquals("Apple iPhone 14 Pro Max", products.getContent().getFirst().getName());
        assertEquals("Apple iPhone 18 Pro Max", products.getContent().get(1).getName());
    }

    @Test
    void modifying() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            int total = productRepository.deleteProductUsingName("Wrong");
            assertEquals(0, total);

            total = productRepository.updateProductPriceToZero(3L);
            assertEquals(1, total);

            Product product = productRepository.findById(3L).orElse(null);
            assertNotNull(product);
            assertEquals(0L, product.getPrice());
        });
    }

    @Test
    void stream() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            Category category = categoryRepository.findById(27L).orElse(null);
            assertNotNull(category);

            productRepository.streamAllByCategory(category).forEach(product ->
                    System.out.println(product.getName()));
        });
    }

    @Test
    void slice() {
        Pageable firstPage = PageRequest.of(0, 2);

        Category category = categoryRepository.findById(27L).orElse(null);
        assertNotNull(category);

        Slice<Product> slice = productRepository.findAllByCategory(category, firstPage);

        while (slice.hasNext()) {
            slice = productRepository.findAllByCategory(category, slice.nextPageable());

            slice.forEach(product -> {
                System.out.println(product.getName());
            });
        }
    }

    @Test
    void lock1() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            try {
                Product product = productRepository.findFirstByIdEquals(1L).orElse(null);
                assertNotNull(product);
                product.setPrice(30_000_000L);

                Thread.sleep(5_000L);
                productRepository.save(product);
            } catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    @Test
    void lock2() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            Product product = productRepository.findFirstByIdEquals(1L).orElse(null);
            assertNotNull(product);
            product.setPrice(10_000_000L);
            productRepository.save(product);
        });
    }

    @Test
    void specification() {
        List<Product> products = productRepository.findAll((root, query, criteriaBuilder) -> query.where(
                    criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("name"), "Apple iPhone 14 Pro Max"),
                            criteriaBuilder.equal(root.get("name"), "Apple iPhone 13 Pro Max")
                    )
            ).getRestriction());
        assertEquals(2, products.size());
    }

    @Test
    void projection() {
        List<SimpleProduct> simpleProducts = productRepository.findAllByNameLike("%Apple%", SimpleProduct.class);
        assertEquals(4, simpleProducts.size());

        List<ProductPrice> productPrices = productRepository.findAllByNameLike("%Apple%", ProductPrice.class);
        assertEquals(4, productPrices.size());
    }
}