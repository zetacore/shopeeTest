package com.shopeeTest.repository;

import com.shopeeTest.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate,String> {
    List<ExchangeRate> findAllByParentAndMarkForDeleteFalse(String parent);
    List<ExchangeRate> findAllByMarkForDeleteFalse();
}
