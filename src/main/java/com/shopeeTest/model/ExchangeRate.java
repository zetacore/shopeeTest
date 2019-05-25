package com.shopeeTest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "exchange_rate")
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate implements Serializable {
    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Column(name = "exchange_from")
    private String exchangeFrom;

    @Column(name = "exchange_to")
    private String exchangeTo;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "parent")
    private String parent;

    @Column(name = "mark_for_delete")
    @Builder.Default
    private boolean markForDelete = false;

}
