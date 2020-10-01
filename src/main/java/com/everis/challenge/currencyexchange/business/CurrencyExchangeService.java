package com.everis.challenge.currencyexchange.business;

import java.util.Date;
import java.util.List;
import com.everis.challenge.currencyexchange.expose.request.CurrencyExchangeRequest;
import com.everis.challenge.currencyexchange.expose.request.ProfilesCurrency;
import com.everis.challenge.currencyexchange.model.CurrencyOperation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyExchangeService {

  public Flux<List<CurrencyOperation>> getAllRealTimeExchanges();

  public void createCurrencyExchange(CurrencyExchangeRequest request);

  public Mono<CurrencyOperation> getExchangesProfile(ProfilesCurrency profilesCurrency, String date);

}
