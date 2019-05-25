package com.shopeeTest.service;

import com.shopeeTest.dto.ExchangeRateDTO;
import com.shopeeTest.dto.ForeignExchangeDTO;
import com.shopeeTest.model.ExchangeRate;
import com.shopeeTest.model.ForeignExchange;
import com.shopeeTest.repository.ExchangeRateRepository;
import com.shopeeTest.repository.ForeignExchangeRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ForeignExchangeServiceBeanTest {
    private static String DEFAULT_EXCHANGE_FROM = "exchange_from";
    private static String DEFAULT_EXCHANGE_TO = "exchange_to";
    private static Double DEFAULT_RATE = 0.1d;
    private static Date DEFAULT_DATE = new Date();
    private static String DEFAULT_LOCAL_DATE = "11/11/2011";

    @Mock
    private ForeignExchangeRepository foreignExchangeRepository;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @InjectMocks
    private ForeignExchangeService foreignExchangeService = new ForeignExchangeServiceBean();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void teardown() {
        Mockito.verifyNoMoreInteractions(this.foreignExchangeRepository);
        Mockito.verifyNoMoreInteractions(this.exchangeRateRepository);
    }

    private ForeignExchangeDTO generateForeignExchangeDTO() {
        List exchangeRatesDTOs = new ArrayList();
        exchangeRatesDTOs.add(ExchangeRateDTO.builder().build());
        return ForeignExchangeDTO.builder().date(ForeignExchangeServiceBeanTest.DEFAULT_DATE).
                exchangeRateDTOS(exchangeRatesDTOs).build();
    }

    private ExchangeRate generateExchangeRate() {
        return ExchangeRate.builder().rate(ForeignExchangeServiceBeanTest.DEFAULT_RATE)
                .exchangeFrom(ForeignExchangeServiceBeanTest.DEFAULT_EXCHANGE_FROM)
                .exchangeTo(ForeignExchangeServiceBeanTest.DEFAULT_EXCHANGE_TO).build();
    }


    private ForeignExchange generateForeignExchange() {
        List<ExchangeRate> exchangeRates = Arrays.asList(generateExchangeRate());
        return ForeignExchange.builder().date(ForeignExchangeServiceBeanTest.DEFAULT_DATE)
                .exchangeRates(exchangeRates).build();
    }

    private List<ForeignExchange> generateForeignExchanges() {
        List<ForeignExchange> foreignExchanges = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            ForeignExchange foreignExchange = this.generateForeignExchange();
            List<ExchangeRate> exchangeRates = new ArrayList<>();
            exchangeRates.add(generateExchangeRate());
            foreignExchange.setExchangeRates(exchangeRates);
            foreignExchanges.add(foreignExchange);
        }
        return foreignExchanges;
    }

    private LocalDate generateLocalDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(ForeignExchangeServiceBeanTest.DEFAULT_LOCAL_DATE, formatter);
    }

    @Test
    public void create_Valid_Success() {
        this.foreignExchangeService.create(this.generateForeignExchangeDTO());
        Mockito.verify(this.foreignExchangeRepository).save(Mockito.any(ForeignExchange.class));
    }

    @Test
    public void findByDate_Valid_Success() {
        Mockito.when(this.exchangeRateRepository.findAllByParentAndMarkForDeleteFalse(Mockito.anyString()))
                .thenReturn(Arrays.asList(generateExchangeRate()));
        Mockito.when(this.foreignExchangeRepository
                .findTop7ByDateLessThanEqualAndMarkForDeleteFalseOrderByDateDesc(Mockito.any(Date.class)))
                .thenReturn(generateForeignExchanges());
        this.foreignExchangeService.findByDate(this.generateLocalDate());
        Mockito.verify(this.foreignExchangeRepository)
                .findTop7ByDateLessThanEqualAndMarkForDeleteFalseOrderByDateDesc(Mockito.any(Date.class));
        Mockito.verify(this.exchangeRateRepository, Mockito.times(8))
                .findAllByParentAndMarkForDeleteFalse(Mockito.anyString());
    }

    @Test
    public void findByDate_InsufficientData_Success() {
        ExchangeRate exchangeRate = this.generateExchangeRate();
        exchangeRate.setRate(0d);
        Mockito.when(this.exchangeRateRepository.findAllByParentAndMarkForDeleteFalse(Mockito.anyString()))
                .thenReturn(Arrays.asList(exchangeRate));
        Mockito.when(this.foreignExchangeRepository
                .findTop7ByDateLessThanEqualAndMarkForDeleteFalseOrderByDateDesc(Mockito.any(Date.class)))
                .thenReturn(generateForeignExchanges());
        this.foreignExchangeService.findByDate(this.generateLocalDate());
        Mockito.verify(this.foreignExchangeRepository)
                .findTop7ByDateLessThanEqualAndMarkForDeleteFalseOrderByDateDesc(Mockito.any(Date.class));
        Mockito.verify(this.exchangeRateRepository, Mockito.times(2))
                .findAllByParentAndMarkForDeleteFalse(Mockito.anyString());
    }

    @Test
    public void recentExchangeRate_Valid_Success() {
        Mockito.when(this.exchangeRateRepository.findAllByParentAndMarkForDeleteFalse(Mockito.anyString()))
                .thenReturn(Arrays.asList(generateExchangeRate()));
        Mockito.when(this.foreignExchangeRepository
                .findTop7ByDateLessThanEqualAndMarkForDeleteFalseOrderByDateDesc(Mockito.any(Date.class)))
                .thenReturn(generateForeignExchanges());
        this.foreignExchangeService
                .recentExchangeRate(ForeignExchangeServiceBeanTest.DEFAULT_EXCHANGE_FROM,
                        ForeignExchangeServiceBeanTest.DEFAULT_EXCHANGE_TO);
        Mockito.verify(this.exchangeRateRepository, Mockito.times(14))
                .findAllByParentAndMarkForDeleteFalse(Mockito.anyString());
        Mockito.verify(this.foreignExchangeRepository)
                .findTop7ByDateLessThanEqualAndMarkForDeleteFalseOrderByDateDesc(Mockito.any(Date.class));
    }

    @Test
    public void recentExchangeRate_InsufficientData_Success() {
        ExchangeRate exchangeRate = this.generateExchangeRate();
        exchangeRate.setRate(0d);
        Mockito.when(this.exchangeRateRepository.findAllByParentAndMarkForDeleteFalse(Mockito.anyString()))
                .thenReturn(Arrays.asList(exchangeRate));
        Mockito.when(this.foreignExchangeRepository
                .findTop7ByDateLessThanEqualAndMarkForDeleteFalseOrderByDateDesc(Mockito.any(Date.class)))
                .thenReturn(generateForeignExchanges());
        this.foreignExchangeService
                .recentExchangeRate(ForeignExchangeServiceBeanTest.DEFAULT_EXCHANGE_FROM,
                        ForeignExchangeServiceBeanTest.DEFAULT_EXCHANGE_TO);
        Mockito.verify(this.foreignExchangeRepository)
                .findTop7ByDateLessThanEqualAndMarkForDeleteFalseOrderByDateDesc(Mockito.any(Date.class));
        Mockito.verify(this.exchangeRateRepository, Mockito.times(8))
                .findAllByParentAndMarkForDeleteFalse(Mockito.anyString());
    }

    @Test
    public void addExchangeRate_Success_Success() {
        List<ForeignExchange> foreignExchanges = new ArrayList<>();
        Mockito.when(this.foreignExchangeRepository.findAllByMarkForDeleteFalse())
                .thenReturn(foreignExchanges);
        this.foreignExchangeService.addExchangeRate(ForeignExchangeServiceBeanTest.DEFAULT_EXCHANGE_FROM,
                ForeignExchangeServiceBeanTest.DEFAULT_EXCHANGE_TO);
        Mockito.verify(this.foreignExchangeRepository).findAllByMarkForDeleteFalse();
        Mockito.verify(this.foreignExchangeRepository, Mockito.times(foreignExchanges.size())).save(Mockito.any());
    }
}
