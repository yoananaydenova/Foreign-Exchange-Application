package com.yoanan.foreignexchangeapp.repository;

import com.yoanan.foreignexchangeapp.model.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {

    Optional<TransactionEntity> findByIdAndDate(String id, LocalDate date);

    Page<TransactionEntity> findAllByDate(LocalDate date, Pageable pageable);

}
