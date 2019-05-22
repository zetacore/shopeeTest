package com.shopeeTest.service;

import com.shopeeTest.dto.ForeignExchangeDTO;
import com.shopeeTest.view.FindByDateView;
import com.shopeeTest.view.RecentView;

import java.time.LocalDate;

public interface ForeignExchangeService {
    void create(ForeignExchangeDTO dto);
    FindByDateView findByDate(LocalDate localDate);
    RecentView recentExchangeRate(String exchangeFrom, String exchangeTo);
    void addExchangeRate(String exchangeFrom, String exchangeTo);
}
