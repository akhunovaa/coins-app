package ru.leon4uk.app.bot.impl;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Buy implements Runnable{

    private final static Logger logger = Logger.getLogger(Buy.class);

    private double marge;
    private double minAskPriceTwo;
    private int rialtoId;
    private String pair;

    public Buy() {
    }


    @Override
    public void run() {
        logger.info("buy");
    }

    public double getMarge() {
        return marge;
    }

    public void setMarge(double marge) {
        this.marge = marge;
    }

    public double getMinAskPriceTwo() {
        return minAskPriceTwo;
    }

    public void setMinAskPriceTwo(double minAskPriceTwo) {
        this.minAskPriceTwo = minAskPriceTwo;
    }

    public int getRialtoId() {
        return rialtoId;
    }

    public void setRialtoId(int rialtoId) {
        this.rialtoId = rialtoId;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }


}
