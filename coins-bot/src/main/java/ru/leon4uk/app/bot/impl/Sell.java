package ru.leon4uk.app.bot.impl;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.leon4uk.coins.service.ApiService;

import java.io.IOException;

@Component
@Scope("prototype")
public class Sell implements Runnable {
    private final static Logger logger = Logger.getLogger(Sell.class);
    private ApiService rialto;
    private double marge;
    private double maxBidPriceTwo;
    private int rialtoId;
    private String pair;
    private double fee = 0.25;

    public Sell() {
    }

    @Override
    public void run() {
        logger.info("SELL: MARGE-" + marge + " PRICE-" + maxBidPriceTwo + " PAIR-" + pair + "CURRENCY_SELL-" + "LTC");
        double balance = 0.0;
        double sellPrice = maxBidPriceTwo;
        try {
            balance = Double.valueOf(rialto.getBitsaneBalance("XRP"));
            double sellAmount = balance * sellPrice;
            rialto.newOrder(pair, sellAmount, sellPrice, "sell");
        } catch (IOException e) {
            logger.error("Ошибка получения баланса/выставлении ордера валюты", e);
        }
        logger.info("SOLD: MARGE-" + marge + " PRICE-" + maxBidPriceTwo + " PAIR-" + pair + "CURRENCY_SELL-" + "LTC");
    }

    private double sell(double sellPrice, double sellAmount, double sellFee) {
        double buy = sellAmount * sellPrice;
        double fee = sellAmount / 100 * sellFee;
        return buy - fee;
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

    public ApiService getRialto() {
        return rialto;
    }

    public void setRialto(ApiService rialto) {
        this.rialto = rialto;
    }
}
