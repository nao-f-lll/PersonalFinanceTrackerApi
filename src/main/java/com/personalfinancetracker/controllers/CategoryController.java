package com.personalfinancetracker.controllers;

import com.personalfinancetracker.domain.dto.BankAccountDto;
import com.personalfinancetracker.domain.dto.CategoryDto;
import com.personalfinancetracker.domain.entities.BankAccountEntity;
import com.personalfinancetracker.domain.entities.CategoryEntity;
import com.personalfinancetracker.mapper.Mapper;
import com.personalfinancetracker.service.BankAccountService;
import com.personalfinancetracker.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;
    private final Mapper<CategoryEntity, CategoryDto> categoryMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, Mapper<CategoryEntity, CategoryDto> categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping(path = "/v1/categories")
    public Page<CategoryDto> getCategories(Pageable pageable) {
        Page<CategoryEntity> categoryEntities = categoryService.findAll(pageable);
        return categoryEntities.map(categoryMapper::mapTo);
    }

}
