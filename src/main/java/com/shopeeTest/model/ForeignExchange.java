package com.shopeeTest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "foreign_exchange")
@NoArgsConstructor
@AllArgsConstructor
public class ForeignExchange implements Serializable {
    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Column(name = "date")
    private Date date;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @Builder.Default
    List<ExchangeRate> exchangeRates = new ArrayList<>();

    @Column(name = "mark_for_delete")
    @Builder.Default
    private boolean markForDelete = false;
}
