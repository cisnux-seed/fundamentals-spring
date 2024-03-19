package dev.cisnux.springjpa;

import jakarta.persistence.EntityManagerFactory;
import lombok.Cleanup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EntityManagerTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void testEntityManagerFactory() {
        Assertions.assertNotNull(entityManagerFactory);

        @Cleanup final var entityManager = entityManagerFactory.createEntityManager();
        Assertions.assertNotNull(entityManager);
    }
}
