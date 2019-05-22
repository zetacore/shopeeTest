package com.shopeeTest.service;

import com.shopeeTest.model.ExchangeRate;
import com.shopeeTest.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ExchangeRateServiceBean implements ExchangeRateService {
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Override
    public void delete(String exchangeFrom, String exchangeTo) {
        List<ExchangeRate> exchangeRates = this.exchangeRateRepository.findAllByMarkForDeleteFalse();
        for (int i = 0; i < exchangeRates.size(); i++) {
            ExchangeRate exchangeRate = exchangeRates.get(i);
            if (exchangeRate.getExchangeFrom().equals(exchangeFrom) &&
                    exchangeRate.getExchangeTo().equals(exchangeTo)) {
                exchangeRate.setMarkForDelete(true);
                this.exchangeRateRepository.save(exchangeRate);
            }
        }
    }
}
