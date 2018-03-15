package ru.leon4uk.app.bot.impl.polo;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.leon4uk.app.bot.impl.buffer.BitsaneBuyOrderBuffer;
import ru.leon4uk.app.bot.telegram.Telegram;
import ru.leon4uk.coins.service.ApiService;
import ru.leon4uk.coins.service.poloniex.entity.PoloniexOrder;

import java.io.IOException;
import java.text.DecimalFormat;

@Component
@Scope("prototype")
public class BuyPolo implements Runnable {

    private final static Logger logger = Logger.getLogger(BuyPolo.class);
    private ApplicationContext context;

    private ApiService rialto;
    private double marge;
    private double minAskPrice;
    private int rialtoId;
    private String pair;


    public BuyPolo() {
    }

    @Override
    public void run() {
        logger.info("BUY: MARGE-" + marge + " PRICE-" + minAskPrice + " PAIR-" + pair + "CURRENCY_BUY-" + "LTC");
        double balance = 0.0;
        double buyPrice = minAskPrice;
        double buyAmount = 0;
        PoloniexOrder poloniexOrder = null;
        try {
            balance = Double.valueOf(rialto.getBalance("USDT"));
            buyAmount = balance / buyPrice;
            buyAmount = Math.round(buyAmount*100000)/100000.0;
            poloniexOrder = rialto.makePoloOrder(pair, buyAmount, buyPrice, "buy");
            logger.info("Response message \n" + poloniexOrder);
        } catch (IOException e) {
            logger.error("Ошибка получения баланса/выставлении ордера валюты", e);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (poloniexOrder != null) {
            logger.info("BUY ORDER" + poloniexOrder.getOrderNumber() + " RESULT: \n " + poloniexOrder.getResultingTrades());
        }

            logger.info("BOUGHT: MARGE-" + marge + " PRICE-" + minAskPrice + " PAIR-" + pair + "CURRENCY_BUY-" + "LTC");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<b>[CREATE ORDER BUY]</b>").append("\n");
            stringBuilder.append("<b>QTY: </b>").append(buyAmount).append(" [").append(pair).append("] ").append(" (").append(buyPrice).append(") ").append(" [").append(minAskPrice).append("] ").append("\n");
            stringBuilder.append("<b>Diff: </b>").append(new DecimalFormat("#.#####").format(marge)).append("\n");
        if (poloniexOrder != null) {
            stringBuilder.append("<b>ID: </b>").append(poloniexOrder.getOrderNumber()).append("\n");
            stringBuilder.append("<b>RESULT: </b>").append("\n").append(poloniexOrder.getResultingTrades()).append("\n");
        }
        context.getBean(Telegram.class).sendMessage(stringBuilder.toString());

        synchronized (this) {
            this.notify();
            logger.info("Thread Buy notify!");
        }
    }


    public double getMarge() {
        return marge;
    }

    public void setMarge(double marge) {
        this.marge = marge;
    }

    public double getMinAskPrice() {
        return minAskPrice;
    }

    public void setMinAskPrice(double minAskPriceTwo) {
        this.minAskPrice = minAskPriceTwo;
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
