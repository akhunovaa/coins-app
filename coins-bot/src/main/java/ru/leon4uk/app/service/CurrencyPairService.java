package ru.leon4uk.app.service;

import ru.leon4uk.app.domain.CurrencyPair;

import java.util.List;

public interface CurrencyPairService {

    public void addCurrencyPair(CurrencyPair currencyPair);

    public List<CurrencyPair> listCurrencyPair();

    public void removeCurrencyPair(Integer id);
}
