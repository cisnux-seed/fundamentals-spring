package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.MultiFoo;
import dev.cisnux.learnspringframework.repositories.CategoryRepository;
import dev.cisnux.learnspringframework.repositories.CustomerRepository;
import dev.cisnux.learnspringframework.repositories.ProductRepository;
import dev.cisnux.learnspringframework.services.CustomerService;
import dev.cisnux.learnspringframework.services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class ComponentTest {
    private ConfigurableApplicationContext applicationContext;

    @BeforeAll
    void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(ComponentConfiguration.class);
        applicationContext.registerShutdownHook();
    }

    @Test
    void testService() {
        final var productService1 = applicationContext.getBean(ProductService.class);
        final var productService2 = applicationContext.getBean("productService", ProductService.class);

        Assertions.assertSame(productService1, productService2);
    }

    @Test
    void testConstructorDependencyInjection(){
        final var productRepository = applicationContext.getBean(ProductRepository.class);
        final var productService = applicationContext.getBean(ProductService.class);

        Assertions.assertSame(productRepository, productService.getProductRepository());
    }

    @Test
    void testSetterDependencyInjection() {
        final var categoryRepository = applicationContext.getBean(CategoryRepository.class);
        final var productService = applicationContext.getBean(ProductService.class);

        Assertions.assertSame(categoryRepository, productService.getCategoryRepository());
    }

    @Test
    void testFieldDependencyInjection() {
        final var customerService = applicationContext.getBean(CustomerService.class);

        final var normalCustomerRepository = applicationContext.getBean("normalCustomerRepository", CustomerRepository.class);
        final var premiumCustomerRepository = applicationContext.getBean("premiumCustomerRepository",CustomerRepository.class);

        Assertions.assertSame(normalCustomerRepository, customerService.getNormalCustomerRepository());
        Assertions.assertSame(premiumCustomerRepository, customerService.getPremiumCustomerRepository());
    }

    @Test
    void testObjectProvider() {
        final var multiFoo = applicationContext.getBean(MultiFoo.class);
        Assertions.assertEquals(3, multiFoo.getFoos().size());
    }
}
