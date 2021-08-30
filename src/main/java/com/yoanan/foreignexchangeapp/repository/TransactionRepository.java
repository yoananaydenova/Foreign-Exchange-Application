package com.yoanan.foreignexchangeapp.repository;

import com.yoanan.foreignexchangeapp.ui.model.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {

}
