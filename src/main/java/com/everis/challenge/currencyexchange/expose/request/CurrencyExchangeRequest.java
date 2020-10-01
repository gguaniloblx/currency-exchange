package com.everis.challenge.currencyexchange.expose.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyExchangeRequest {
  private Double sell;
  private Double buy;
  private String date;
}
