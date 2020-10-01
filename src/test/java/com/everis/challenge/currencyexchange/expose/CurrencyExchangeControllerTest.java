package com.everis.challenge.currencyexchange.expose;

import java.util.ArrayList;
import java.util.List;
import com.everis.challenge.currencyexchange.business.impl.CurrencyExchangeServiceImpl;
import com.everis.challenge.currencyexchange.expose.request.CurrencyExchangeRequest;
import com.everis.challenge.currencyexchange.model.CurrencyExchangeModel;
import com.everis.challenge.currencyexchange.model.CurrencyOperation;
import com.everis.challenge.currencyexchange.repository.CurrencyExchangeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = CurrencyExchangeController.class)
@Import(CurrencyExchangeServiceImpl.class)
class CurrencyExchangeControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private CurrencyExchangeRepository currencyExchangeRepository;

  @MockBean
  private Cache cache;;

  @Test
  void createCurrencyExchangeTest() {
    CurrencyExchangeRequest request = CurrencyExchangeRequest.builder().buy(3.05)
        .sell(3.05)
        .date("28-09-2020").build();
    webTestClient.post().uri("/exchange")
        .body(BodyInserters.fromObject(request))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  void getAllRealTimeExchangesTest() {
    List<CurrencyExchangeModel> all = new ArrayList<>();
    CurrencyExchangeModel model = CurrencyExchangeModel.builder()
        .buy(3.20).id(1L).sell(3.30).date("2020-09-29").build();
    all.add(model);
    given(currencyExchangeRepository.findAll()).willReturn(all);
    webTestClient
        .get()
        .uri("/exchange/realtime")
        .accept(MediaType.APPLICATION_STREAM_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(CurrencyOperation.class)
        .consumeWith(
            exchangeResult ->
                exchangeResult.getResponseBody().forEach(currencyExchangeResponse -> {
                      assertEquals(currencyExchangeResponse.getBuy(), Double.valueOf(3.20));
                      assertEquals(currencyExchangeResponse.getSell(), Double.valueOf(3.30));
                      assertEquals(currencyExchangeResponse.getDate(), "2020-09-29");
                    }
                ));
  }

  @Test
  public void getExchangeStaticsTest() {
    CurrencyExchangeModel profileLow = CurrencyExchangeModel.builder()
        .buy(3.05).date("28-09-2020").sell(3.05).build();
    given(currencyExchangeRepository.findProfile(anyString(), anyString())).willReturn(profileLow);
    webTestClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/exchange/{profile}").queryParam("date", "28-09-2020")
            .build("LOW")
        )
        .accept(MediaType.APPLICATION_STREAM_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody(CurrencyOperation.class)
        .consumeWith(
            exchangeResult -> {
              assertEquals(exchangeResult.getResponseBody().getBuy(), Double.valueOf(3.05));
              assertEquals(exchangeResult.getResponseBody().getSell(), Double.valueOf(3.05));
            }
        );
  }
}