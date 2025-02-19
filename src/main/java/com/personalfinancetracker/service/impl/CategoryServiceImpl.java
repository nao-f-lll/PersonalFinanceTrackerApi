package com.personalfinancetracker.service.impl;

import com.personalfinancetracker.domain.entities.CategoryEntity;
import com.personalfinancetracker.enums.Category;
import com.personalfinancetracker.repositories.CategoryRepository;
import com.personalfinancetracker.service.CategoryService;
import jakarta.annotation.PostConstruct;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final String CACHE_TABLE_NAME = "categories";


    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostConstruct
    public void initCategories() {
        for (Category parentCategory : Category.values()) {
            if (!categoryRepository.existsByName(parentCategory.getName())) {
                CategoryEntity parentCategoryEntity = new CategoryEntity(null, parentCategory.getName(), null, null, true, parentCategory.getDescription(), parentCategory.getType());
                categoryRepository.save(parentCategoryEntity);
                List<CategoryEntity> subCategories = new ArrayList<>();
                for (String subCategory : parentCategory.getSubCategories()) {
                    CategoryEntity subCategoryEntity = new CategoryEntity(null,subCategory, parentCategoryEntity, null, true, "", parentCategory.getType());
                    categoryRepository.save(subCategoryEntity);
                    subCategories.add(subCategoryEntity);
                }
                CategoryEntity savedParentCategory = categoryRepository.findByName(parentCategoryEntity.getName());
                savedParentCategory.setSubCategories(subCategories);
                categoryRepository.save(savedParentCategory);
            }
        }
    }

    @Cacheable(value = CACHE_TABLE_NAME)
    @Override
    public Page<CategoryEntity> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Cacheable(value = CACHE_TABLE_NAME)
    @Override
    public Page<CategoryEntity> findParentCategories(Pageable pageable) {
        return categoryRepository.findByParentCategoryIsNull(pageable);
    }

    @Cacheable(value = CACHE_TABLE_NAME)
    @Override
    public Page<CategoryEntity> findSubCategoriesByParentId(Pageable pageable, Long parentId) {
        return categoryRepository.findSubCategoriesByParentCategoryId(pageable, parentId);
    }

    @Override
    public Optional<CategoryEntity> findOne(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }
}