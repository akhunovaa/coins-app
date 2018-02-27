package ru.leon4uk.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.leon4uk.app.dao.CurrencyPairDAO;
import ru.leon4uk.app.domain.CurrencyPair;

import java.util.List;

@Service
public class CurrencyPairServiceImpl implements CurrencyPairService{

    @Autowired
    private CurrencyPairDAO currencyPairDAO;

    @Override
    public void addCurrencyPair(CurrencyPair currencyPair) {
        currencyPairDAO.addCurrencyPair(currencyPair);
    }

    @Override
    public List<CurrencyPair> listCurrencyPair() {
        return currencyPairDAO.listCurrencyPair();
    }

    @Override
    public void removeCurrencyPair(Integer id) {
        currencyPairDAO.removeCurrencyPair(id);
    }
}
