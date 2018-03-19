package ru.leon4uk.app.bot.impl.polo;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.leon4uk.app.bot.rocket.Rocket;
import ru.leon4uk.app.bot.telegram.Telegram;
import ru.leon4uk.coins.service.ApiService;
import ru.leon4uk.coins.service.poloniex.entity.PoloniexOrder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private Boolean fail;

    private Future<?> periodicOrderHandler;

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
            balance = Double.valueOf(rialto.getBalance("USDT")) - 0.02;
            buyAmount = balance / buyPrice;
            buyAmount = Math.round(buyAmount * 100000) / 100000.0;
            poloniexOrder = rialto.makePoloOrder(pair, buyAmount, buyPrice, "buy");
            logger.info("Response message \n" + poloniexOrder);
        } catch (IOException e) {
            logger.error("Ошибка получения баланса/выставлении ордера валюты", e);
            context.getBean(Rocket.class).sendMessage("Ошибка получения баланса/выставления ордера валюты " + e.getMessage());
        }

        if (null != poloniexOrder) {
            logger.info("BUY ORDER" + poloniexOrder.getOrderNumber() + "BALANCE: " + balance + " BUY AMOUNT " + buyAmount + " RESULT: \n " + poloniexOrder.getResultingTrades());
            OrderHandler orderHandler = new OrderHandler();
            orderHandler.setContext(context);
            orderHandler.setMarge(marge);
            orderHandler.setOrderNumber(poloniexOrder.getOrderNumber());
            orderHandler.setPair(pair);
            orderHandler.setPrice(buyPrice);
            orderHandler.setRialto(rialto);
            orderHandler.setFail(Boolean.FALSE);
            periodicOrderHandler = context.getBean(ScheduledExecutorService.class).scheduleWithFixedDelay(orderHandler, 20, 5000, TimeUnit.MILLISECONDS);
            orderHandler.setFuture(periodicOrderHandler);
            orderHandler.setFlag(Boolean.FALSE);
            synchronized (orderHandler.getFuture()) {
                    try {
                        logger.info("wait!");
                        orderHandler.getFuture().wait();
                        logger.info("waited!");
                        logger.info("stop!");
                    } catch (InterruptedException e) {
                        logger.error("Ошибка при остановке просмотрщика ордеров", e);
                        context.getBean(Rocket.class).sendMessage("Ошибка при остановке просмотрщика ордеров " + e.getMessage());
                    }
            }
            int count = 0;

            while (orderHandler.getFail()) {
                orderHandler.setCount(0);
                try {
                    String orderCancelResult = rialto.orderCancel(String.valueOf(poloniexOrder.getOrderNumber()));
                    logger.info("orderCancelResult " + orderCancelResult);
                    context.getBean(Rocket.class).sendMessage("Отмена ордера на покупку " + orderCancelResult);
                    buyPrice += 0.10;
                    buyAmount = balance / buyPrice;
                    buyAmount = Math.round(buyAmount * 100000) / 100000.0;
                    poloniexOrder = rialto.makePoloOrder(pair, buyAmount, buyPrice, "buy");
                    logger.info("Response message \n" + poloniexOrder);
                    orderHandler.setPrice(buyPrice);
                    periodicOrderHandler = context.getBean(ScheduledExecutorService.class).scheduleWithFixedDelay(orderHandler, 20, 5000, TimeUnit.MILLISECONDS);
                    orderHandler.setFuture(periodicOrderHandler);
                    orderHandler.setFlag(Boolean.FALSE);
                    synchronized (orderHandler.getFuture()) {
                        try {
                            logger.info("wait!");
                            orderHandler.getFuture().wait();
                            logger.info("waited!");
                            logger.info("stop!");
                        } catch (InterruptedException e) {
                            logger.error("Ошибка при остановке просмотрщика ордеров", e);
                            context.getBean(Rocket.class).sendMessage("Ошибка при остановке просмотрщика ордеров " + e.getMessage());
                        }
                    }
                    count ++;
                } catch (IOException e) {
                    logger.error("Ошибка получения баланса/выставлении ордера валюты", e);
                    context.getBean(Rocket.class).sendMessage("Ошибка получения баланса/выставлении ордера валюты " + e.getMessage());
                }
                if (count >= 10){
                    orderHandler.setFail(Boolean.FALSE);
                    this.fail = Boolean.TRUE;
                    logger.info("Order fail: " + poloniexOrder.getOrderNumber() + " with the price " + buyPrice);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("<b>[ORDER FAIL]</b>").append("\n");
                    stringBuilder.append("<b>ID: </b>").append(poloniexOrder.getOrderNumber()).append(" [").append(pair).append("] ").append("\n");
                    context.getBean(Rocket.class).sendMessage(stringBuilder.toString());
                    logger.info("Order fail: " + poloniexOrder.getOrderNumber() + " with the price " + buyPrice);
                    count = 0;
                }
            }
            while(this.fail){
                logger.info("WAIT's for manual odrer execution. Please BUY some LTC " + marge + " PRICE: " + minAskPrice + " PAIR: " + pair);
                Double usdtBalance = 0.0;
                Double ltBalance = 0.0;
                try {
                    usdtBalance = Double.valueOf(rialto.getBalance("USDT"));
                    ltBalance = Double.valueOf(rialto.getBalance("LTC"));
                } catch (IOException e) {
                    logger.error("Ошибка получения баланса/выставлении ордера валюты", e);
                    context.getBean(Rocket.class).sendMessage("Ошибка получения баланса/выставлении ордера валюты " + e.getMessage());
                }
                if (usdtBalance < 1.0 && ltBalance >= 0.02){
                    logger.info("Poloniex Balance is ok. Checked. Continue to work now! + usdtBalance " + usdtBalance + "  ltBalance " + ltBalance);
                    context.getBean(Rocket.class).sendMessage("Poloniex Balance is ok. Checked. Continue to work now! usdtBalance " + usdtBalance + "  ltBalance " + ltBalance);
                    this.fail = Boolean.FALSE;
                }
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!this.fail){
                logger.info("BOUGHT! MARGE: " + marge + " PRICE: " + minAskPrice + "(" + buyPrice + ")" +" PAIR: " + pair + "CURRENCY_BUY: " + "LTC");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("<b>[BUY]</b>").append("\n");
                stringBuilder.append("<b>QTY: </b>").append(buyAmount).append(" [").append(pair).append("] ").append(" (").append(buyPrice).append(") ").append(" [").append(minAskPrice).append("] ").append("\n");
                stringBuilder.append("<b>Diff: </b>").append(new DecimalFormat("#.#####").format(marge)).append("\n");
                stringBuilder.append("<b>ID: </b>").append(poloniexOrder.getOrderNumber()).append("\n");
                stringBuilder.append("<b>RESULT: </b>").append("\n").append(poloniexOrder.getResultingTrades()).append("\n");
                context.getBean(Rocket.class).sendMessage(stringBuilder.toString());
                synchronized (this) {
                    this.notify();
                    logger.info("Thread Buy notify!");
                }
            }
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

    public synchronized Boolean getFail() {
        return fail;
    }

    public synchronized void setFail(Boolean fail) {
        this.fail = fail;
    }
}
