package com.everis.challenge.currencyexchange.business.impl;

import java.util.ArrayList;
import java.util.List;
import com.everis.challenge.currencyexchange.expose.request.CurrencyExchangeRequest;
import com.everis.challenge.currencyexchange.expose.request.ProfilesCurrency;
import com.everis.challenge.currencyexchange.model.CurrencyExchangeModel;
import com.everis.challenge.currencyexchange.model.CurrencyOperation;
import com.everis.challenge.currencyexchange.repository.CurrencyExchangeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.cache.Cache;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
public class CurrencyExchangeServiceImplTest {

  @InjectMocks
  CurrencyExchangeServiceImpl currencyExchangeService;

  @Mock
  CurrencyExchangeRepository currencyExchangeRepository;

  @Mock
  Cache cache;

  @Before
  public void setup() {
  }

  @Test
  public void getAllRealTimeExchangesTest() {
    List<CurrencyExchangeModel> all = new ArrayList<>();
    CurrencyExchangeModel model = CurrencyExchangeModel.builder()
        .buy(3.20).date("28-09-2020").id(1L).sell(3.30).date("2020-09-29").build();
    all.add(model);
    Mockito.when(currencyExchangeRepository.findAll()).thenReturn(all);
    Flux<List<CurrencyOperation>> allRealTimeExchanges = currencyExchangeService.getAllRealTimeExchanges();
    allRealTimeExchanges.subscribe(generalResponse -> {
      assertEquals(generalResponse.get(0).getBuy(), Double.valueOf(3.20));
      assertEquals(generalResponse.get(0).getSell(), Double.valueOf(3.30));
      assertEquals(generalResponse.get(0).getDate(), "2020-09-29");
    });
  }

  @Test
  public void getExchangesProfileTest() {
    CurrencyExchangeModel profileLow = CurrencyExchangeModel.builder()
        .buy(3.05).date("28-09-2020").sell(3.05).build();
    Mockito.when(currencyExchangeRepository.findProfile(anyString(), anyString())).thenReturn(profileLow);
    Mono<CurrencyOperation> exchangesProfile = currencyExchangeService.getExchangesProfile(ProfilesCurrency.LOW, "28-09-2020");
    exchangesProfile.subscribe(generalResponse -> {
      assertEquals(generalResponse.getBuy(), Double.valueOf(3.05));
      assertEquals(generalResponse.getSell(), Double.valueOf(3.05));
    });
  }

  @Test
  public void createCurrencyExchangeTest() {
    CurrencyExchangeRequest request = CurrencyExchangeRequest.builder().buy(3.05)
        .sell(3.05)
        .date("28-09-2020").build();
    request.setDate("28-09-2020");
    currencyExchangeService.createCurrencyExchange(request);
  }

}