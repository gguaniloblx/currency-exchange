package com.everis.challenge.currencyexchange.repository;

import java.util.List;
import com.everis.challenge.currencyexchange.model.CurrencyExchangeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchangeModel, Long> {

  @Query(nativeQuery = true)
  public List<CurrencyExchangeModel> findAll();

  @Query(nativeQuery = true)
  public CurrencyExchangeModel findProfile(@Param("profile") String profile, @Param("date") String date);

}
