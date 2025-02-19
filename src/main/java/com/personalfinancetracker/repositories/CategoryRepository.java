package com.personalfinancetracker.repositories;

import com.personalfinancetracker.domain.entities.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<CategoryEntity, Long>,
        PagingAndSortingRepository<CategoryEntity, Long> {

    boolean existsByName(String name);

    CategoryEntity findByName(String name);

    Page<CategoryEntity> findByParentCategoryIsNull(Pageable pageable);

    Page<CategoryEntity> findSubCategoriesByParentCategoryId(Pageable pageable, Long parentId);
}
