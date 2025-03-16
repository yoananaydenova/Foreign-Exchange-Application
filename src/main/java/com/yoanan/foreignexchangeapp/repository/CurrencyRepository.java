package com.yoanan.foreignexchangeapp.repository;

import com.yoanan.foreignexchangeapp.model.entity.CurrencyCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyCodeEntity, String> {
}
