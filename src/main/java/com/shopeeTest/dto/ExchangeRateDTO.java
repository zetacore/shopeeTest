package com.shopeeTest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ExchangeRateDTO {
    private String exchangeFrom;
    private String exchangeTo;
    private Double rate;
}
