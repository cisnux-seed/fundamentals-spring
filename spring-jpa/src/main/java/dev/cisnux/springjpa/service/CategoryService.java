package dev.cisnux.springjpa.service;

import dev.cisnux.springjpa.entity.Category;
import dev.cisnux.springjpa.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionOperations;

@Service
public class CategoryService {
    private CategoryRepository repository;
    private TransactionOperations transactionOperations;
    private PlatformTransactionManager transactionManager;

    @Autowired
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Autowired
    public void setTransactionOperations(TransactionOperations transactionOperations) {
        this.transactionOperations = transactionOperations;
    }

    @Autowired
    public void setService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional
//    @Transactional(propagation = Propagation.MANDATORY)
    public void create() {
        for (int i = 0; i < 5; i++) {
            Category category = new Category();
            category.setName("Category " + i);
            repository.save(category);
        }
        throw new RuntimeException("Ups rollback please");
    }

    public void error() {
        throw new RuntimeException("ups");
    }

    public void createCategories() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
                    for (int i = 0; i < 5; i++) {
                        Category category = new Category();
                        category.setName("Category " + i);
                        repository.save(category);
                    }
                    error();
                }
        );
    }

    public void test() {
        create();
    }

    public void manual() {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setTimeout(10);
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transaction = transactionManager.getTransaction(defaultTransactionDefinition);

        try {
            for (int i = 0; i < 5; i++) {
                Category category = new Category();
                category.setName("Category " + i);
                repository.save(category);
            }
            error();
            transactionManager.commit(transaction);
        } catch (Throwable throwable) {
            transactionManager.rollback(transaction);
            throw throwable;
        }
    }
}
