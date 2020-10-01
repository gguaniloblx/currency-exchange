package com.everis.challenge.currencyexchange.expose;

import java.util.List;
import com.everis.challenge.currencyexchange.business.CurrencyExchangeService;
import com.everis.challenge.currencyexchange.expose.request.CurrencyExchangeRequest;
import com.everis.challenge.currencyexchange.expose.request.ProfilesCurrency;
import com.everis.challenge.currencyexchange.model.CurrencyOperation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/exchange")
@Slf4j
@OpenAPIDefinition(info =
@Info(
    title = "Api Currency Exchanges",
    version = "1.0",
    description = "Api Challenge Everis",
    license = @License(name = "Apache 2.0", url = "http://foo.bar"),
    contact = @Contact(url = "http://gigantic-server.com", name = "Gustavo Guanilo", email = "gguanilo@gmail.com")
)
)
public class CurrencyExchangeController {

  @Autowired
  private CurrencyExchangeService service;

  @PostMapping
  @Operation(summary = "Add Currency Exchanges",
      description = "Add Currency Exchanges")
  public void createCurrencyExchange(@RequestBody CurrencyExchangeRequest request) {
    service.createCurrencyExchange(request);
  }

  @GetMapping(value = "/realtime", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  @Operation(summary = "Get all Exchanges in realtime",
      description = "Get all Exchanges")
  public Flux<List<CurrencyOperation>> getAllRealTimeExchanges() {
    return service.getAllRealTimeExchanges();
  }

  @GetMapping(value = "/{profiles}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  @Operation(summary = "Get all Exchanges Statics",
      description = "Get all Exchanges Statics")
  public Mono<CurrencyOperation> getExchangeStatics(@PathVariable("profiles") ProfilesCurrency profiles,
                                                    @RequestParam("date") String date) {
    return service.getExchangesProfile(profiles, date);
  }

}
