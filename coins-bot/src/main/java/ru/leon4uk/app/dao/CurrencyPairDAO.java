package ru.leon4uk.app.dao;

import ru.leon4uk.app.domain.CurrencyPair;

import java.util.List;

public interface CurrencyPairDAO {

    public void addCurrencyPair(CurrencyPair currencyPair);

    public List<CurrencyPair> listCurrencyPair();

    public void removeCurrencyPair(Integer id);
}
