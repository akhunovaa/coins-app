package ru.leon4uk.app.bot.impl.polo;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.leon4uk.app.bot.telegram.Telegram;
import ru.leon4uk.coins.service.ApiService;
import ru.leon4uk.coins.service.poloniex.entity.PoloniexOrder;

import java.io.IOException;
import java.text.DecimalFormat;


@Component
@Scope("prototype")
public class Sell implements Runnable {
    private final static Logger logger = Logger.getLogger(Sell.class);
    private ApplicationContext context;

    private ApiService rialto;
    private double marge;
    private double maxBidPrice;
    private int rialtoId;
    private String pair;

    public Sell() {
    }

    @Override
    public void run() {
        logger.info("SELL: MARGE-" + marge + " PRICE-" + maxBidPrice + " PAIR-" + pair + "CURRENCY_SELL-" + "LTC");
        double balance = 0.0;
        double sellPrice = maxBidPrice;
        double sellAmount = 0;
        PoloniexOrder poloniexOrder = null;

        try {
            balance = Double.valueOf(rialto.getBitsaneBalance("LTC"));
            sellAmount = balance;
            poloniexOrder = rialto.makePoloOrder(pair, sellAmount, sellPrice, "sell");
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
            logger.info("SELL ORDER" + poloniexOrder.getOrderNumber() + " RESULT: \n " + poloniexOrder.getResultingTrades());
        }

            logger.info("SOLD: MARGE-" + marge + " PRICE-" + sellPrice + " PAIR-" + pair + "CURRENCY_SELL-" + "USDT");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<b>[CREATE ORDER SELL]</b>").append("\n");
            stringBuilder.append("<b>QTY: </b>").append(sellAmount).append(" [").append(pair).append("] ").append(" (").append(sellPrice).append(") ").append(" [").append(maxBidPrice).append("] ").append("\n");
            stringBuilder.append("<b>Diff: </b>").append(new DecimalFormat("#.#####").format(marge)).append("\n");
            if (poloniexOrder != null) {
            stringBuilder.append("<b>ID: </b>").append(poloniexOrder.getOrderNumber()).append("\n");
            stringBuilder.append("<b>RESULT: </b>").append("\n").append(poloniexOrder.getResultingTrades()).append("\n");
        }
            context.getBean(Telegram.class).sendMessage(stringBuilder.toString());

        synchronized (this) {
            this.notify();
            logger.info("Thread Sell notify!");
        }
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
        return maxBidPrice;
    }

    public void setMaxBidPriceTwo(double maxBidPriceTwo) {
        this.maxBidPrice = maxBidPriceTwo;
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
