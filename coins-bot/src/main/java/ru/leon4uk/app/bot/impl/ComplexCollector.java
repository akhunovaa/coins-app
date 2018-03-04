package ru.leon4uk.app.bot.impl;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.leon4uk.coins.service.ApiService;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope("prototype")
public class ComplexCollector implements Runnable{

    private final static Logger logger = Logger.getLogger(ComplexCollector.class);
    private ApiService firstRialto;
    private ApiService firstRialtoHelp;
    private ApiService secondRialto;
    private String firstCurrencyPairOne;
    private String firstCurrencyPairTwo;
    private String secondCurrencyPair;
    private ConcurrentHashMap<String, Double> tradeValues;

    public ComplexCollector() {

    }

    @Override
    public void run() {
        tradeValues = new ConcurrentHashMap<>();
        try {

           logger.info(firstRialto.getAskPrices(firstCurrencyPairOne));
           logger.info(firstRialtoHelp.getAskPrices(firstCurrencyPairTwo));
           logger.info(secondRialto.getAskPrices(secondCurrencyPair));

        } catch (IOException e) {
            logger.error("Ошибка в запросе API",e);
        }

    }

    public ApiService getFirstRialto() {
        return firstRialto;
    }

    public void setFirstRialto(ApiService firstRialto) {
        this.firstRialto = firstRialto;
    }

    public ApiService getFirstRialtoHelp() {
        return firstRialtoHelp;
    }

    public void setFirstRialtoHelp(ApiService firstRialtoHelp) {
        this.firstRialtoHelp = firstRialtoHelp;
    }

    public ApiService getSecondRialto() {
        return secondRialto;
    }

    public void setSecondRialto(ApiService secondRialto) {
        this.secondRialto = secondRialto;
    }

    public String getFirstCurrencyPairOne() {
        return firstCurrencyPairOne;
    }

    public void setFirstCurrencyPairOne(String firstCurrencyPairOne) {
        this.firstCurrencyPairOne = firstCurrencyPairOne;
    }

    public String getFirstCurrencyPairTwo() {
        return firstCurrencyPairTwo;
    }

    public void setFirstCurrencyPairTwo(String firstCurrencyPairTwo) {
        this.firstCurrencyPairTwo = firstCurrencyPairTwo;
    }

    public String getSecondCurrencyPair() {
        return secondCurrencyPair;
    }

    public void setSecondCurrencyPair(String secondCurrencyPair) {
        this.secondCurrencyPair = secondCurrencyPair;
    }
}
