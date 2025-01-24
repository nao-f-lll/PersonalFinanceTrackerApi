package com.personalfinancetracker.repositories;

import com.personalfinancetracker.domain.entities.BankAccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccountEntity, Long>,
        PagingAndSortingRepository<BankAccountEntity, Long> {

    @Query("SELECT a FROM BankAccountEntity a WHERE a.userEntity.id = :userId")
    Page<BankAccountEntity> findAllByUserId(Pageable pageable,  @Param("userId") Long userId);

    @Query("SELECT COUNT(a) > 0 FROM BankAccountEntity a WHERE a.userEntity.id = :userId AND a.id = :bankAccountId")
    boolean existsById(@Param("userId") Long userId, @Param("bankAccountId") Long bankAccountId);
}
