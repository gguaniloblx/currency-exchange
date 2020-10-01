package com.everis.challenge.currencyexchange.business.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.everis.challenge.currencyexchange.business.CurrencyExchangeService;
import com.everis.challenge.currencyexchange.expose.request.CurrencyExchangeRequest;
import com.everis.challenge.currencyexchange.expose.request.ProfilesCurrency;
import com.everis.challenge.currencyexchange.model.CurrencyExchangeModel;
import com.everis.challenge.currencyexchange.model.CurrencyOperation;
import com.everis.challenge.currencyexchange.repository.CurrencyExchangeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;
import reactor.cache.CacheMono;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

@Service
@Slf4j
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

  private CurrencyExchangeRepository currencyExchangeRepository;

  private Cache cache;

  public CurrencyExchangeServiceImpl(Cache cache, CurrencyExchangeRepository currencyExchangeRepository) {
    this.cache = cache;
    this.currencyExchangeRepository = currencyExchangeRepository;
  }

  @Override
  public Flux<List<CurrencyOperation>> getAllRealTimeExchanges() {
    return Flux.just(currencyExchangeRepository.findAll().stream().map(currency -> CurrencyOperation.builder()
        .buy(currency.getBuy())
        .sell(currency.getSell())
        .date(currency.getDate()).build())
        .collect(Collectors.toList()));
  }

  @Override
  public void createCurrencyExchange(CurrencyExchangeRequest request) {
    currencyExchangeRepository.save(CurrencyExchangeModel.builder()
        .sell(request.getSell())
        .buy(request.getBuy())
        .date(request.getDate()).build());
  }

  @Override
  public Mono<CurrencyOperation> getExchangesProfile(ProfilesCurrency profilesCurrency, String date) {
    String id = profilesCurrency.name().concat(date);
    return CacheMono.lookup(key -> Mono.justOrEmpty(cache.get(id, CurrencyOperation.class))
        .map(Signal::next), id)
        .onCacheMissResume(() -> Mono.just(getProfileModel(profilesCurrency, date)))
        .andWriteWith((key, signal) -> Mono.fromRunnable(() ->
            Optional.ofNullable(signal.get())
                .ifPresent(value -> cache.put(key, value))));
  }

  private CurrencyOperation getProfileModel(ProfilesCurrency profilesCurrency, String date) {
    final CurrencyExchangeModel profile = currencyExchangeRepository.findProfile(profilesCurrency.name(), date);
    return CurrencyOperation.builder().buy(profile.getBuy()).sell(profile.getSell()).build();
  }

}
