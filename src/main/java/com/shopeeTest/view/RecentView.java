package com.shopeeTest.view;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Data
public class RecentView {
    String average;
    Double varience;
    @Builder.Default
    List<Date> dates = new ArrayList<>();
    @Builder.Default
    List<Double> rates = new ArrayList<>();
}
