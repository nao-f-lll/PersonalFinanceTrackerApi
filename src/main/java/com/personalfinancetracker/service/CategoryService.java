package com.personalfinancetracker.service;

import com.personalfinancetracker.domain.entities.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface CategoryService {
    Page<CategoryEntity> findAll(Pageable pageable);

    Page<CategoryEntity> findParentCategories(Pageable pageable);

    Page<CategoryEntity> findSubCategoriesByParentId(Pageable pageable, Long parentId);

    Optional<CategoryEntity> findOne(Long id);
}
