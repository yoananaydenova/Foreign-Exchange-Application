package com.yoanan.foreignexchangeapp.repository;

import com.yoanan.foreignexchangeapp.model.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, String> {
}
