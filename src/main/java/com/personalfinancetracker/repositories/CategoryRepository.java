package com.personalfinancetracker.repositories;

import com.personalfinancetracker.domain.entities.CategoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<CategoryEntity, Long>,
        PagingAndSortingRepository<CategoryEntity, Long> {

    boolean existsByName(String name);

    CategoryEntity findByName(String name);
}
