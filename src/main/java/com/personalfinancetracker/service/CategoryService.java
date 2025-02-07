package com.personalfinancetracker.service;

import com.personalfinancetracker.domain.entities.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public interface CategoryService {
    Page<CategoryEntity> findAll(Pageable pageable);
}
