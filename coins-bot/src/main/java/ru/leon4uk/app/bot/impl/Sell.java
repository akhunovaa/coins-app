package ru.leon4uk.app.bot.impl;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Sell implements Runnable {
    private final static Logger logger = Logger.getLogger(Buy.class);

    private double marge;
    private double maxBidPriceTwo;
    private int rialtoId;
    private String pair;

    public Sell() {
    }

    @Override
    public void run() {
        logger.info("sell");
    }

    public double getMarge() {
        return marge;
    }

    public void setMarge(double marge) {
        this.marge = marge;
    }

    public double getMaxBidPriceTwo() {
        return maxBidPriceTwo;
    }

    public void setMaxBidPriceTwo(double maxBidPriceTwo) {
        this.maxBidPriceTwo = maxBidPriceTwo;
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
