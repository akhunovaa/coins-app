package ru.leon4uk.app.bot;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.leon4uk.app.service.CurrencyPairService;

@Component
public class BotManager implements BotApplication {

    private final static Logger logger = Logger.getLogger(BotManager.class);

    private String currencyPair;
    private int firstRialto;
    private int secondRialto;

    @Autowired
    private CurrencyPairService currencyPairService;

    public BotManager() {
    }

    public static Logger getLogger() {
        return logger;
    }


    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public int getFirstRialto() {
        return firstRialto;
    }

    public void setFirstRialto(int firstRialto) {
        this.firstRialto = firstRialto;
    }

    public int getSecondRialto() {
        return secondRialto;
    }

    public void setSecondRialto(int secondRialto) {
        this.secondRialto = secondRialto;
    }
}