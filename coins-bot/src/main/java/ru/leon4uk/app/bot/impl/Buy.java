package ru.leon4uk.app.bot.impl;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.leon4uk.app.bot.impl.buffer.BitsaneBuyOrderBuffer;
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
        context.getBean(BitsaneBuyOrderBuffer.class).setStatus(Boolean.TRUE);
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
                if ("buy".equals(order.getSide()) && pair.toLowerCase().equals(order.getPair().toLowerCase())){
                    context.getBean(BitsaneBuyOrderBuffer.class).setId(order.getId());
                    context.getBean(BitsaneBuyOrderBuffer.class).setPair(order.getPair());
                    context.getBean(BitsaneBuyOrderBuffer.class).setSide(order.getSide());
                }
            }
        }
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BitsaneOrder bitsaneOrder = null;
        try {
            bitsaneOrder = rialto.getBitsaneOrder(context.getBean(BitsaneBuyOrderBuffer.class).getId());
        } catch (IOException e) {
            logger.error("Ошибка при запросе на получение статуса ордера", e);
        }
        if (bitsaneOrder != null && bitsaneOrder.isLive()) {
            logger.info("BUY FAIL: MARGE-" + marge + " PRICE-" + minAskPriceTwo + " PAIR-" + pair + "CURRENCY_BUY-" + "XRP");
            logger.warn("ID " + bitsaneOrder.getId() + " PAIR " + bitsaneOrder.getPair() + " executed_amount " + bitsaneOrder.getExecutedAmount() + " remaining_amount " + bitsaneOrder.getRemainingAmount() + " original_amount " + bitsaneOrder.getOriginalAmount());
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<b>").append(dateFormat.format(date)).append("</b>").append("\n");
            stringBuilder.append(rialtoId).append("\n");
            stringBuilder.append("<b>Ордер не исполнился: </b>BUY").append("\n");
            stringBuilder.append("<b>ID: </b>").append(bitsaneOrder.getId()).append("\n");
            stringBuilder.append("<b>PAIR: </b>").append(bitsaneOrder.getPair()).append("\n");
            stringBuilder.append("<b>executed_amount: </b>").append(bitsaneOrder.getExecutedAmount()).append("\n");
            stringBuilder.append("<b>remaining_amount: </b>").append(bitsaneOrder.getRemainingAmount()).append("\n");
            stringBuilder.append("<b>original_amount: </b>").append(bitsaneOrder.getOriginalAmount()).append("\n");
            context.getBean(Telegram.class).sendMessage(stringBuilder.toString());
        }else {
            logger.info("BOUGHT: MARGE-" + marge + " PRICE-" + minAskPriceTwo + " PAIR-" + pair + "CURRENCY_SELL-" + "XRP");
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
            context.getBean(BitsaneBuyOrderBuffer.class).setStatus(Boolean.FALSE);
        }
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
