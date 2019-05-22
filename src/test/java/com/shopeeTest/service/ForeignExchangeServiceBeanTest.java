package com.shopeeTest.service;

import com.shopeeTest.dto.ExchangeRateDTO;
import com.shopeeTest.dto.ForeignExchangeDTO;
import com.shopeeTest.model.ExchangeRate;
import com.shopeeTest.model.ForeignExchange;
import com.shopeeTest.repository.ForeignExchangeRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ForeignExchangeServiceBeanTest {
    @Mock
    private ForeignExchangeRepository foreignExchangeRepository;

    @InjectMocks
    private ForeignExchangeService foreignExchangeService = new ForeignExchangeServiceBean();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void teardown() {
        Mockito.verifyNoMoreInteractions(this.foreignExchangeRepository);
    }

    private ForeignExchangeDTO generateForeignExchangeDTO() {
        List exchangeRatesDTOs = new ArrayList();
        exchangeRatesDTOs.add(ExchangeRateDTO.builder().build());
        return ForeignExchangeDTO.builder().date(new Date()).exchangeRateDTOS(exchangeRatesDTOs).build();
    }

    @Test
    public void create_Valid_Success() {
        this.foreignExchangeService.create(generateForeignExchangeDTO());
        Mockito.verify(this.foreignExchangeRepository).save(Mockito.any(ForeignExchange.class));
    }


}
