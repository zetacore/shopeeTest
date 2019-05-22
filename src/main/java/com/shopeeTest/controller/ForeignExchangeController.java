package com.shopeeTest.controller;

import com.google.common.base.Preconditions;
import com.shopeeTest.dto.ForeignExchangeDTO;
import com.shopeeTest.service.ExchangeRateService;
import com.shopeeTest.service.ForeignExchangeService;
import com.shopeeTest.view.FindByDateView;
import com.shopeeTest.view.RecentView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = ForeignExchangeControllerConstant.root)
public class ForeignExchangeController {

    @Autowired
    private ForeignExchangeService foreignExchangeService;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @RequestMapping(value = ForeignExchangeControllerConstant.create, method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody ForeignExchangeDTO foreignExchangeInput) {
        Preconditions.checkArgument(foreignExchangeInput != null);
        this.foreignExchangeService.create(foreignExchangeInput);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = ForeignExchangeControllerConstant.findByDate, method = RequestMethod.GET)
    public ResponseEntity findByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime) {
        Preconditions.checkArgument(localDateTime != null);
        FindByDateView findByDateView = this.foreignExchangeService.findByDate(localDateTime.toLocalDate());
        Preconditions.checkArgument(findByDateView != null);
        return new ResponseEntity<>(findByDateView, HttpStatus.OK);
    }

    @RequestMapping(value =ForeignExchangeControllerConstant.recentExchangeRate, method = RequestMethod.GET)
    public ResponseEntity recentExchangeRate(@RequestParam String currencyFrom, @RequestParam String currencyTo) {
        RecentView recentView = this.foreignExchangeService.recentExchangeRate(currencyFrom, currencyTo);
        return new ResponseEntity<>(recentView, HttpStatus.OK);
    }

    @RequestMapping(value = ForeignExchangeControllerConstant.addExchangeRate, method = RequestMethod.GET)
    public ResponseEntity addExchangeRate(@RequestParam String exchangeFrom, @RequestParam String exchangeTo){
        this.foreignExchangeService.addExchangeRate(exchangeFrom,exchangeTo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = ForeignExchangeControllerConstant.delete, method = RequestMethod.GET)
    public ResponseEntity delete(@RequestParam String exchangeFrom, @RequestParam String exchangeTo){
        this.exchangeRateService.delete(exchangeFrom,exchangeTo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
