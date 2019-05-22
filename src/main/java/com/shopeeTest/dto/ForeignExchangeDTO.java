package com.shopeeTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ForeignExchangeDTO{
    private Date date;
    private List<ExchangeRateDTO> exchangeRateDTOS;
}
