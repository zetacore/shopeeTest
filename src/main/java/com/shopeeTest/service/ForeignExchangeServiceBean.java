package com.shopeeTest.service;

import com.google.common.base.Preconditions;
import com.shopeeTest.dto.ExchangeRateDTO;
import com.shopeeTest.dto.ForeignExchangeDTO;
import com.shopeeTest.model.ExchangeRate;
import com.shopeeTest.model.ForeignExchange;
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
    ForeignExchangeRepository foreignExchangeRepository;

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
        ForeignExchange foreignExchangeByDate = this.foreignExchangeRepository.findByDateAndMarkForDeleteFalse(date);
        List<ExchangeRate> exchangeRates = foreignExchangeByDate.getExchangeRates();
        List<ForeignExchange> foreignExchanges = this.foreignExchangeRepository
                .findTop7ByDateLessThanEqualAndMarkForDeleteFalseOrderByDateDesc(date);
        int exchangeRateCount = foreignExchangeByDate.getExchangeRates().size();
        Preconditions.checkArgument(exchangeRateCount > 0);
        for (int i = 0; i < exchangeRateCount; i++) {
            ExchangeRate current = exchangeRates.get(i);
            if (exchangeRates.get(i).getRate() == 0 || exchangeRates.get(i).getRate() == null) {
                findByDateView.getRate().add("insufficient_data");
            } else {
                findByDateView.getRate().add(exchangeRates.get(i).getRate().toString());
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
            for (int j = 0; j < foreignExchanges.get(i).getExchangeRates().size(); j++) {
                ExchangeRate currentExchangeRate = foreignExchanges.get(i).getExchangeRates().get(j);
                if (exchangeFrom.equals(currentExchangeRate.getExchangeFrom())
                        && exchangeTo.equals(currentExchangeRate.getExchangeTo())) {
                    tempRates.add(currentExchangeRate.getRate());
                }
            }
        }
        RecentView recentView = RecentView.builder().dates(tempDates).rates(tempRates).build();
        recentView.setAverage(this.average(foreignExchanges, exchangeFrom, exchangeTo));
        Double varience = Collections.max(tempRates) - Collections.min(tempRates);
        recentView.setVarience(varience);
        return recentView;
    }

    @Override
    public void addExchangeRate(String exchangeFrom, String exchangeTo) {
        List<ForeignExchange> foreignExchanges = this.foreignExchangeRepository.findAllByMarkForDeleteFalse();
        for (int i = 0; i < foreignExchanges.size(); i++) {
            ExchangeRate newExchangeRate = ExchangeRate.builder().exchangeFrom(exchangeFrom).exchangeTo(exchangeTo).rate(0d)
                    .build();
            foreignExchanges.get(i).getExchangeRates().add(newExchangeRate);
            this.foreignExchangeRepository.save(foreignExchanges.get(i));
        }
    }


    public String average(List<ForeignExchange> foreignExchanges, String exchangeFrom, String exchangeTo) {
        double sum = 0;
        for (int i = 0; i < foreignExchanges.size(); i++) {
            List<ExchangeRate> exchangeRates = foreignExchanges.get(i).getExchangeRates();
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
