package ru.leon4uk.app.bot.impl;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;;
import ru.leon4uk.coins.service.ApiService;

import java.io.IOException;

@Component
@Scope("prototype")
public class Buy implements Runnable {

    private final static Logger logger = Logger.getLogger(Buy.class);
    private ApiService rialto;
    private double marge;
    private double minAskPriceTwo;
    private int rialtoId;
    private String pair;
    private double fee = 0.25;

    public Buy() {
    }

    @Override
    public void run() {
        logger.info("BUY: MARGE-" + marge + " PRICE-" + minAskPriceTwo + " PAIR-" + pair + "CURRENCY_BUY-" + "XRP");
        double balance = 0.0;
        double buyPrice = minAskPriceTwo;
        try {
            balance = Double.valueOf(rialto.getBitsaneBalance("LTC"));
            double buyAmount = balance / buyPrice;
            rialto.newOrder(pair, buyAmount, buyPrice, "buy");
        } catch (IOException e) {
            logger.error("Ошибка получения баланса/выставлении ордера валюты", e);
        }
        logger.info("BOUGHT: MARGE-" + marge + " PRICE-" + minAskPriceTwo + " PAIR-" + pair + "CURRENCY_BUY-" + "XRP");

    }

    private double buy(double buyPrice, double buyAmount, double buyFee) {
        double buy = buyAmount / buyPrice;
        double fee = buy / 100 * buyFee;
        return buy - fee;
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

    public ApiService getRialto() {
        return rialto;
    }

    public void setRialto(ApiService rialto) {
        this.rialto = rialto;
    }
}
