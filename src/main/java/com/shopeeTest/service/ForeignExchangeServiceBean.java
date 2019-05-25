package com.shopeeTest.service;

import com.google.common.base.Preconditions;
import com.shopeeTest.dto.ExchangeRateDTO;
import com.shopeeTest.dto.ForeignExchangeDTO;
import com.shopeeTest.model.ExchangeRate;
import com.shopeeTest.model.ForeignExchange;
import com.shopeeTest.repository.ExchangeRateRepository;
import com.shopeeTest.repository.ForeignExchangeRepository;
import com.shopeeTest.view.FindByDateView;
import com.shopeeTest.view.RecentView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ForeignExchangeServiceBean implements ForeignExchangeService {

    @Autowired
    private ForeignExchangeRepository foreignExchangeRepository;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Override
    public void create(ForeignExchangeDTO dto) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        for (int i = 0; i < dto.getExchangeRateDTOS().size(); i++) {
            ExchangeRateDTO rateDTO = dto.getExchangeRateDTOS().get(i);
            ExchangeRate tempExchangeRate = ExchangeRate.builder().exchangeFrom(rateDTO.getExchangeFrom())
                    .exchangeTo(rateDTO.getExchangeTo()).rate(rateDTO.getRate()).build();
            exchangeRates.add(tempExchangeRate);
        }
        ForeignExchange foreignExchange =
                ForeignExchange.builder().date(dto.getDate()).exchangeRates(exchangeRates).build();
        this.foreignExchangeRepository.save(foreignExchange);
    }

    @Override
    public FindByDateView findByDate(LocalDate localDate) {
        Date date = java.sql.Date.valueOf(localDate);
        FindByDateView findByDateView = FindByDateView.builder().build();
        List<ForeignExchange> foreignExchanges = this.foreignExchangeRepository
                .findTop7ByDateLessThanEqualAndMarkForDeleteFalseOrderByDateDesc(date);
        List<ExchangeRate> exchangeRates = this.exchangeRateRepository
                .findAllByParentAndMarkForDeleteFalse(foreignExchanges.get(0).getId());
        for (int i = 0; i < exchangeRates.size(); i++) {
            ExchangeRate current = exchangeRates.get(i);
            if (current.getRate() == 0 || current.getRate() == null) {
                findByDateView.getRate().add("insufficient_data");
            } else {
                findByDateView.getRate().add(current.getRate().toString());
            }
            findByDateView.getAverage()
                    .add(this.average(foreignExchanges, current.getExchangeFrom(), current.getExchangeTo()));
            findByDateView.getExchangeFrom().add(current.getExchangeFrom());
            findByDateView.getExchangeTo().add(current.getExchangeTo());
        }
        return findByDateView;
    }

    @Override
    public RecentView recentExchangeRate(String exchangeFrom, String exchangeTo) {
        List<ForeignExchange> foreignExchanges = this.foreignExchangeRepository
                .findTop7ByDateLessThanEqualAndMarkForDeleteFalseOrderByDateDesc(new Date());
        List<Date> tempDates = new ArrayList<>();
        List<Double> tempRates = new ArrayList<>();
        for (int i = 0; i < foreignExchanges.size(); i++) {
            tempDates.add(foreignExchanges.get(i).getDate());
            List<ExchangeRate> exchangeRates = exchangeRateRepository
                    .findAllByParentAndMarkForDeleteFalse(foreignExchanges.get(i).getId());
            for (int j = 0; j < exchangeRates.size(); j++) {
                if (exchangeFrom.equals(exchangeRates.get(j).getExchangeFrom())
                        && exchangeTo.equals(exchangeRates.get(j).getExchangeTo())) {
                    tempRates.add(exchangeRates.get(j).getRate());
                }
            }
        }
        RecentView recentView = RecentView.builder().dates(tempDates).rates(tempRates).build();
        recentView.setAverage(this.average(foreignExchanges, exchangeFrom, exchangeTo));
        Double variance = Collections.max(tempRates) - Collections.min(tempRates);
        recentView.setVariance(variance);
        return recentView;
    }

    @Override
    public void addExchangeRate(String exchangeFrom, String exchangeTo) {
        List<ForeignExchange> foreignExchanges = this.foreignExchangeRepository.findAllByMarkForDeleteFalse();
        for (int i = 0; i < foreignExchanges.size(); i++) {
            ExchangeRate newExchangeRate = ExchangeRate.builder().exchangeFrom(exchangeFrom).exchangeTo(exchangeTo)
                    .rate(0d)
                    .build();
            foreignExchanges.get(i).getExchangeRates().add(newExchangeRate);
            this.foreignExchangeRepository.save(foreignExchanges.get(i));
        }
    }


    public String average(List<ForeignExchange> foreignExchanges, String exchangeFrom, String exchangeTo) {
        double sum = 0;
        for (int i = 0; i < foreignExchanges.size(); i++) {
            List<ExchangeRate> exchangeRates = exchangeRateRepository
                    .findAllByParentAndMarkForDeleteFalse(foreignExchanges.get(i).getId());
            for (int j = 0; j < exchangeRates.size(); j++) {
                ExchangeRate exchangeRate = exchangeRates.get(j);
                if (exchangeRate.getExchangeFrom().equals(exchangeFrom) &&
                        exchangeRate.getExchangeTo().equals(exchangeTo)) {
                    if (exchangeRate.getRate() == null || exchangeRate.getRate() == 0) {
                        return "insufficient data";
                    } else {
                        sum += exchangeRate.getRate();
                    }
                }
            }
        }
        return String.valueOf((sum / foreignExchanges.size()));
    }
}
