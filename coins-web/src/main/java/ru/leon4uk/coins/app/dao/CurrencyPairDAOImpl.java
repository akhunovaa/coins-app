package ru.leon4uk.coins.app.dao;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.leon4uk.coins.app.domain.CurrencyPair;

import java.util.List;

@Repository
public class CurrencyPairDAOImpl implements CurrencyPairDAO{

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addCurrencyPair(CurrencyPair currencyPair) {
        sessionFactory.getCurrentSession().save(currencyPair);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CurrencyPair> listCurrencyPair() {
        return sessionFactory.openSession().createQuery("from CurrencyPair").list();
    }

    @Override
    public void removeCurrencyPair(Integer id) {
        CurrencyPair currencyPair = (CurrencyPair) sessionFactory.getCurrentSession().load(CurrencyPair.class, id);
        if (null != currencyPair) {
            sessionFactory.getCurrentSession().delete(currencyPair);
        }
    }
}
