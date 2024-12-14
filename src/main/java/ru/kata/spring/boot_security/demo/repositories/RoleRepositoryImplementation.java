package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RoleRepositoryImplementation {
    @PersistenceContext
    private EntityManager entityManager;
}
