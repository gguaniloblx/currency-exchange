package com.everis.challenge.currencyexchange.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class CurrencyOperation {
  private Double sell;
  private Double buy;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String date;
}
