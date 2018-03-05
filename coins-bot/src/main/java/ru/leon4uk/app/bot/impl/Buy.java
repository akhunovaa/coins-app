package ru.leon4uk.app.bot.impl;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;;
import ru.leon4uk.app.bot.telegram.Telegram;
import ru.leon4uk.coins.service.ApiService;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Scope("prototype")
public class Buy implements Runnable {

    private final static Logger logger = Logger.getLogger(Buy.class);
    private ApplicationContext context;

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
        double buyAmount = 0;
        try {
            balance = Double.valueOf(rialto.getBitsaneBalance("LTC"));
            buyAmount = balance / buyPrice;
            rialto.newOrder(pair, buyAmount, buyPrice, "buy");
        } catch (IOException e) {
            logger.error("Ошибка получения баланса/выставлении ордера валюты", e);
        }
        logger.info("BOUGHT: MARGE-" + marge + " PRICE-" + minAskPriceTwo + " PAIR-" + pair + "CURRENCY_BUY-" + "XRP");
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<b>").append(dateFormat.format(date)).append("</b>").append("\n");
        stringBuilder.append(rialtoId).append("\n");
        stringBuilder.append("<b>Тип операции: </b>BUY").append("\n");
        stringBuilder.append("<b>Цена: </b>").append(buyPrice).append("\n");
        stringBuilder.append("<b>Ордер: </b>").append(buyAmount).append("\n");
        stringBuilder.append("<b>Разница: </b>").append(new DecimalFormat("#.#####").format(marge)).append("/").append("\n");
        context.getBean(Telegram.class).sendMessage(stringBuilder.toString());
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

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }
}
