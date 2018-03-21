package ru.leon4uk.app.bot.impl.polo;


import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.leon4uk.app.bot.rocket.Rocket;
import ru.leon4uk.app.bot.telegram.Telegram;
import ru.leon4uk.coins.service.ApiService;
import ru.leon4uk.coins.service.poloniex.entity.PoloniexOpenOrder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

@Component
@Scope("prototype")
public class OrderHandler implements Runnable {


    private final static Logger logger = Logger.getLogger(OrderHandler.class);
    private ApplicationContext context;

    private ApiService rialto;
    private double marge;
    private double price;
    private Boolean flag;
    private Boolean fail;

    private int rialtoId;
    private int count;
    private String pair;
    private long orderNumber;
    private List<PoloniexOpenOrder> openOrdersList;
    private Future<?> future;

    @Override
    public void run() {
        logger.info("Start control of the order: " + orderNumber + " with the price " + price);
        if (this.isFlag().equals(Boolean.TRUE)) {
            synchronized (this.getFuture()) {
                logger.info("make notify!");
                this.getFuture().cancel(true);
                this.getFuture().notify();
                logger.info("notified!");
            }
        }else {
            try {
                openOrdersList = rialto.getPoloOpenOrdersList(pair);
            } catch (IOException e) {
                logger.error("Order list get : " + orderNumber + " takes an error", e);
                context.getBean(Rocket.class).sendMessage("Order list get : " + orderNumber + " takes an error " + e.getMessage());
            }
            if (null != openOrdersList && openOrdersList.isEmpty()) {
                flag = Boolean.TRUE;
                this.setFail(Boolean.FALSE);
                logger.info("Order completed: " + orderNumber + " with the price " + price);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("*[ORDER COMPLETED]*").append("\n");
                stringBuilder.append("*ID:*").append(orderNumber).append(" [").append(pair).append("] ").append("\n");
                stringBuilder.append("*PRICE:*").append(price).append(" [").append(pair).append("] ").append("\n");
                context.getBean(Rocket.class).sendMessage(stringBuilder.toString());
                logger.info("End control of the order: " + orderNumber + " with the price " + price);
            }else {
                int tri = getCount();
                setCount(++tri);
            }

            if (this.count == 60){
                flag = Boolean.TRUE;
                this.setFail(Boolean.TRUE);
                logger.info("Order fail: " + orderNumber + " with the price " + price);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("*[ORDER FAIL]*").append("\n");
                stringBuilder.append("*ID:*").append(orderNumber).append(" [").append(pair).append("] ").append("\n");
                stringBuilder.append("*INFO:*").append(openOrdersList).append("\n");
                context.getBean(Rocket.class).sendMessage(stringBuilder.toString());
                this.count = 0;
                logger.info("End control of the FAILED order: " + orderNumber + " with the price " + price);
            }
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public ApiService getRialto() {
        return rialto;
    }

    public void setRialto(ApiService rialto) {
        this.rialto = rialto;
    }

    public double getMarge() {
        return marge;
    }

    public void setMarge(double marge) {
        this.marge = marge;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRialtoId() {
        return rialtoId;
    }

    public synchronized Boolean isFlag() {
        return flag;
    }

    public synchronized void setFlag(boolean flag) {
        this.flag = flag;
    }

    public synchronized Boolean getFail() {
        return fail;
    }

    public synchronized void setFail(Boolean fail) {
        this.fail = fail;
    }

    public synchronized int getCount() {
        return count;
    }

    public synchronized void setCount(int count) {
        this.count = count;
    }

    public List<PoloniexOpenOrder> getOpenOrdersList() {
        return openOrdersList;
    }

    public void setOpenOrdersList(List<PoloniexOpenOrder> openOrdersList) {
        this.openOrdersList = openOrdersList;
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

    public synchronized long getOrderNumber() {
        return orderNumber;
    }

    public synchronized void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public synchronized Future<?>  getFuture() {
        return future;
    }

    public synchronized void setFuture(Future<?> future) {
        this.future = future;
    }
}
