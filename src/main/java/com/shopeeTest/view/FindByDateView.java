package com.shopeeTest.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FindByDateView {
    @Builder.Default
    private List<String> exchangeFrom = new ArrayList<>();
    @Builder.Default
    private List<String> exchangeTo = new ArrayList<>();
    @Builder.Default
    private List<String> rate = new ArrayList<>();
    @Builder.Default
    private List<String> average = new ArrayList<>();
}
