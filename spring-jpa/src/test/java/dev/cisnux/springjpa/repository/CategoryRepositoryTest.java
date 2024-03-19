package dev.cisnux.springjpa.repository;

import dev.cisnux.springjpa.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void insert() {
        Category category = new Category();
        category.setName("Fajra");

        categoryRepository.save(category);

        assertNotNull(category);
    }

    @Test
    void update() {
        var category = categoryRepository.findById(27L).orElse(null);

        assertNotNull(category);

        category.setName("new gadget");
        categoryRepository.save(category);

        category = categoryRepository.findById(27L).orElse(null);
        assertNotNull(category);
        assertNotNull("new gadget", category.getName());
    }

    @Test
    void delete() {
        categoryRepository.deleteById(1L);

        var category = categoryRepository.findById(1L).orElse(null);

        assertNull(category);
    }

    @Test
    void queryMethod() {
        Category category = categoryRepository.findFirstByNameEquals("new gadget").orElse(null);
        assertNotNull(category);
        assertEquals("new gadget", category.getName());

        final var categories = categoryRepository.findAllByNameLike("%new%");
        assertEquals(1, categories.size());
        assertEquals("new gadget", categories.getFirst().getName());
    }

    @Test
    void audit() {
        Category category = new Category();
        category.setName("Sample Audit");
        categoryRepository.save(category);

        assertNotNull(category.getId());
        assertNotNull(category.getCreatedAt());
        assertNotNull(category.getLastModifiedAt());
    }

    @Test
    void example1() {
        Category category = new Category();
        category.setName("new gadget");

        Example<Category> example = Example.of(category);

        List<Category> categories = categoryRepository.findAll(example);
        assertEquals(1, categories.size());
    }

    @Test
    void example2() {
        Category category = new Category();
        category.setName("new gadget");
        category.setId(27L);

        Example<Category> example = Example.of(category);

        List<Category> categories = categoryRepository.findAll(example);
        assertEquals(1, categories.size());
    }

    @Test
    void exampleMatcher() {
        Category category = new Category();
        category.setName("new GaDgeT");

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues()
                .withIgnoreCase();

        Example<Category> example = Example.of(category, matcher);

        List<Category> categories = categoryRepository.findAll(example);
        assertEquals(1, categories.size());
    }
}