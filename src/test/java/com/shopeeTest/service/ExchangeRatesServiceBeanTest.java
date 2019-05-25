package com.shopeeTest.service;

import com.shopeeTest.model.ExchangeRate;
import com.shopeeTest.repository.ExchangeRateRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

public class ExchangeRatesServiceBeanTest {
    private static Double DEFAULT_RATE = 0.1d;
    private static String DEFAULT_EXCHANGE_FROM = "exchange_from";
    private static String DEFAULT_EXCHANGE_TO = "exchange_to";

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @InjectMocks
    private ExchangeRateService exchangeRateService = new ExchangeRateServiceBean();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void teardown() {
        Mockito.verifyNoMoreInteractions(this.exchangeRateRepository);
    }

    private ExchangeRate generateExchangeRate() {
        return ExchangeRate.builder().rate(ExchangeRatesServiceBeanTest.DEFAULT_RATE)
                .exchangeFrom(ExchangeRatesServiceBeanTest.DEFAULT_EXCHANGE_FROM)
                .exchangeTo(ExchangeRatesServiceBeanTest.DEFAULT_EXCHANGE_TO).build();
    }

    @Test
    public void delete_Valid_Success() {
        Mockito.when(this.exchangeRateRepository.findAllByMarkForDeleteFalse())
                .thenReturn(Arrays.asList(generateExchangeRate()));
        this.exchangeRateService
                .delete(ExchangeRatesServiceBeanTest.DEFAULT_EXCHANGE_FROM,
                        ExchangeRatesServiceBeanTest.DEFAULT_EXCHANGE_TO);
        Mockito.verify(this.exchangeRateRepository).findAllByMarkForDeleteFalse();
        Mockito.verify(this.exchangeRateRepository).save(Mockito.any());
    }
}
