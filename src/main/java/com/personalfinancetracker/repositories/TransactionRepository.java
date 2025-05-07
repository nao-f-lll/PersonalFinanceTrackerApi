package com.personalfinancetracker.repositories;

import com.personalfinancetracker.domain.entities.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionEntity, Long>,
        PagingAndSortingRepository<TransactionEntity, Long> {

    @Query("SELECT a FROM TransactionEntity a WHERE a.userEntity.id = :userId")
    Page<TransactionEntity> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}
