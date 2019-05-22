package com.shopeeTest.repository;

import com.shopeeTest.model.ForeignExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ForeignExchangeRepository extends JpaRepository<ForeignExchange,String> {
    ForeignExchange findByDateAndMarkForDeleteFalse(Date date);
    List<ForeignExchange> findTop7ByDateLessThanEqualAndMarkForDeleteFalseOrderByDateDesc(Date date);
    List<ForeignExchange> findAllByMarkForDeleteFalse();
}
