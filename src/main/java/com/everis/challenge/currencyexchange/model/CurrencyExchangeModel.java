package com.everis.challenge.currencyexchange.model;

import javax.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NamedNativeQuery(name = "CurrencyExchangeModel.findAll",
    query = "SELECT "
        + "ID, "
        + "BUY, "
        + "SELL, "
        + "DATE_PROCESS FROM currency_exchange_model",
    resultClass = CurrencyExchangeModel.class)
@NamedNativeQuery(name = "CurrencyExchangeModel.findProfile",
    query = "SELECT 0 as ID,SELL,BUY,NULL AS DATE_PROCESS FROM (" +
        "SELECT max(SELL) AS SELL ,max(BUY) AS BUY ,'HIGH' AS PROFILE FROM currency_exchange_model " +
        "WHERE DATE_PROCESS = :date \n" +
        "UNION ALL \n" +
        "SELECT (SUM(SELL) / COUNT(1)) AS SELL ,(SUM(BUY) / COUNT(1)) AS BUY , 'MEDIUM'  AS PROFILE FROM currency_exchange_model " +
        "WHERE DATE_PROCESS = :date \n" +
        "UNION ALL \n" +
        "SELECT min(SELL) AS SELL ,min(BUY) AS BUY ,'LOW' AS PROFILE FROM currency_exchange_model " +
        "WHERE DATE_PROCESS = :date ) " +
        "WHERE PROFILE =:profile",
    resultClass = CurrencyExchangeModel.class)
public class CurrencyExchangeModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Double sell;
  private Double buy;
  @Column(name = "DATE_PROCESS")
  private String date;
}
