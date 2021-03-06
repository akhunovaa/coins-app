package ru.leon4uk.app.bot.impl;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.leon4uk.app.bot.impl.buffer.BitsaneBuyOrderBuffer;
import ru.leon4uk.app.bot.impl.buffer.BitsaneSellOrderBuffer;
import ru.leon4uk.app.bot.rocket.Rocket;
import ru.leon4uk.app.bot.telegram.Telegram;
import ru.leon4uk.coins.service.ApiService;
import ru.leon4uk.coins.service.bitsane.entity.BitsaneOrder;
import ru.leon4uk.coins.service.bitsane.entity.BitsaneOrderResult;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Scope("prototype")
public class Sell implements Runnable {
    private final static Logger logger = Logger.getLogger(Sell.class);
    private ApplicationContext context;

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
        context.getBean(BitsaneSellOrderBuffer.class).setStatus(Boolean.TRUE);
        logger.info("SELL: MARGE-" + marge + " PRICE-" + maxBidPriceTwo + " PAIR-" + pair + "CURRENCY_SELL-" + "LTC");
        double balance = 0.0;
        double sellAmount = 0;
        double sellPrice = maxBidPriceTwo;
        String response = null;

        try {
            balance = Double.valueOf(rialto.getBitsaneBalance("XRP"));
            sellAmount = balance;
//            sellAmount = Math.round(sellAmount*100000)/100000.0;
            response = rialto.newOrder(pair, sellAmount, sellPrice, "sell");
            logger.info("Response message \n" + response);
        } catch (IOException e) {
            logger.error("Ошибка получения баланса/выставлении ордера валюты", e);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<BitsaneOrderResult> orders = new ArrayList<>();
        try {
            orders.addAll(rialto.getBitsaneOrders());
        } catch (IOException e) {
            logger.error("Ошибка при запросе на получение кол-ва ордеров", e);
        }
        if (!orders.isEmpty()){
            for (BitsaneOrderResult order : orders) {
                if ("sell".equals(order.getSide()) && pair.toLowerCase().equals(order.getPair().toLowerCase())){
                    context.getBean(BitsaneSellOrderBuffer.class).setId(order.getId());
                    context.getBean(BitsaneSellOrderBuffer.class).setPair(order.getPair());
                    context.getBean(BitsaneSellOrderBuffer.class).setSide(order.getSide());
                }
            }
        }

        try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BitsaneOrder bitsaneOrder = null;
        try {
            bitsaneOrder = rialto.getBitsaneOrder(context.getBean(BitsaneSellOrderBuffer.class).getId());
        } catch (IOException e) {
            logger.error("Ошибка при запросе на получение статуса ордера", e);
        }

        if (bitsaneOrder != null && bitsaneOrder.isLive()) {
            logger.info("SELL FAIL: MARGE-" + marge + " PRICE-" + maxBidPriceTwo + " PAIR-" + pair + "CURRENCY_SELL-" + "LTC");
            logger.warn("ID " + bitsaneOrder.getId() + " PAIR " + bitsaneOrder.getPair() + " executed_amount " + bitsaneOrder.getExecutedAmount() + " remaining_amount " + bitsaneOrder.getRemainingAmount() + " original_amount " + bitsaneOrder.getOriginalAmount());
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<b>").append(dateFormat.format(date)).append("</b>").append("\n");
            stringBuilder.append(rialtoId).append("\n");
            stringBuilder.append("<b>[FAILED SELL ORDER]</b>").append("\n");
            stringBuilder.append("<b>ID: </b>").append(bitsaneOrder.getId()).append("\n");
            stringBuilder.append("<b>PAIR: </b>").append(bitsaneOrder.getPair()).append("\n");
            stringBuilder.append("<b>PRICE: </b>").append(bitsaneOrder.getPrice()).append("\n");
            stringBuilder.append("<b>executed_amount: </b>").append(bitsaneOrder.getExecutedAmount()).append("\n");
            stringBuilder.append("<b>remaining_amount: </b>").append(bitsaneOrder.getRemainingAmount()).append("\n");
            stringBuilder.append("<b>original_amount: </b>").append(bitsaneOrder.getOriginalAmount()).append("\n");
            context.getBean(Rocket.class).sendMessage(stringBuilder.toString());
        }else {
            logger.info("SOLD: MARGE-" + marge + " PRICE-" + maxBidPriceTwo + " PAIR-" + pair + "CURRENCY_SELL-" + "LTC");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<b>[CREATE ORDER SELL]</b>").append("\n");
            stringBuilder.append("<b>QTY: </b>").append(sellAmount).append(" [").append(pair).append("] ").append(" (").append(sellPrice).append(") ").append(" [").append(maxBidPriceTwo).append("] ").append("\n");
            stringBuilder.append("<b>Diff: </b>").append(new DecimalFormat("#.#####").format(marge)).append("\n");
            if (bitsaneOrder != null)
                stringBuilder.append("<b>ID: </b>").append(bitsaneOrder.getId()).append("\n");
            else
                stringBuilder.append("<b>ID: </b>").append(response).append("\n");
            context.getBean(Rocket.class).sendMessage(stringBuilder.toString());
            context.getBean(BitsaneSellOrderBuffer.class).setStatus(Boolean.FALSE);
        }

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

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }
}
