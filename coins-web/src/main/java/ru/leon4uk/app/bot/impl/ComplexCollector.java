package ru.leon4uk.app.bot.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.leon4uk.app.bot.impl.buffer.BitsaneBuyOrderBuffer;
import ru.leon4uk.app.bot.impl.buffer.BitsaneSellOrderBuffer;
import ru.leon4uk.app.bot.telegram.Telegram;
import ru.leon4uk.app.domain.Statistics;
import ru.leon4uk.app.service.StatisticsService;
import ru.leon4uk.coins.service.ApiService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class ComplexCollector implements Runnable{

    @Autowired
    private StatisticsService statisticsService;

    private ApplicationContext context;

    private boolean reverse;
    private final static Logger logger = Logger.getLogger(ComplexCollector.class);
    private double percentOne = -0.9;
    private double percentTwo = 1.5;
    private ApiService firstRialto;
    private ApiService firstRialtoHelp;
    private ApiService secondRialto;
    private String firstCurrencyPairOne;
    private String firstCurrencyPairTwo;
    private String secondCurrencyPair;
    private int firstRialtoId;
    private int secondRialtoId;
    private Boolean flag;
    private int currencyPairId;
    private Future<?> future;

    public synchronized Future<?>  getFuture() {
        return future;
    }

    public synchronized void setFuture(Future<?> future) {
        this.future = future;
    }

    public ComplexCollector() {

    }

    @Override
    public void run() {

            if (this.isFlag().equals(Boolean.TRUE)) {
                synchronized (this.getFuture()) {
                    logger.info("make notify!");
                    this.getFuture().cancel(true);
                    this.getFuture().notifyAll();
                    logger.info("notified!");
                }
            } else {
                try {
            Statistics statistics = new Statistics();

            List<Double> firstAskPrices = firstRialto.getAskPrices(firstCurrencyPairOne).stream().map(Double::valueOf).collect(Collectors.toList());
            List<Double> firstAskPricesHelp = firstRialtoHelp.getAskPrices(firstCurrencyPairTwo).stream().map(Double::valueOf).collect(Collectors.toList());
            List<Double> secondAskPrices = secondRialto.getAskPrices(secondCurrencyPair).stream().map(Double::valueOf).collect(Collectors.toList());

            List<Double> firstBidPrices = firstRialto.getBidPrices(firstCurrencyPairOne).stream().map(Double::valueOf).collect(Collectors.toList());
            List<Double> firstBidPricesHelp = firstRialtoHelp.getBidPrices(firstCurrencyPairTwo).stream().map(Double::valueOf).collect(Collectors.toList());
            List<Double> secondBidPrices = secondRialto.getBidPrices(secondCurrencyPair).stream().map(Double::valueOf).collect(Collectors.toList());

            double maxBidPriceOneFirst = Collections.max(firstBidPrices);
            double maxBidPriceOneFirstHelp = Collections.max(firstBidPricesHelp);

            double minAskPriceOneFirst = Collections.min(firstAskPrices);
            double minAskPriceOneFirstHelp = Collections.min(firstAskPricesHelp);

            double firstMaxBidOnePrice = maxBidPriceOneFirstHelp / maxBidPriceOneFirst;
            double maxBidPriceSecond = Collections.max(secondBidPrices);

            double firstMinAskOnePrice = minAskPriceOneFirstHelp / minAskPriceOneFirst;
            double minAskPriceSecond = Collections.min(secondAskPrices);

            double margeAskOne = (firstMinAskOnePrice - maxBidPriceSecond)/firstMinAskOnePrice*100;
            double margeBidOne = (firstMaxBidOnePrice - minAskPriceSecond)/firstMaxBidOnePrice*100;
            double margeAskTwo = (minAskPriceSecond - firstMaxBidOnePrice)/minAskPriceSecond*100;
            double margeABidTwo = (maxBidPriceSecond - firstMinAskOnePrice)/maxBidPriceSecond*100;

            statistics.setAskMargeOne(margeAskOne);
            statistics.setAskMargeTwo(margeAskTwo);
            statistics.setBidMargeOne(margeBidOne);
            statistics.setBidMargeTwo(margeABidTwo);
            statistics.setFirstAskPrice(firstMinAskOnePrice);
            statistics.setFirstAskPriceXrp(minAskPriceOneFirstHelp);
            statistics.setFirstAskPrieLtc(minAskPriceOneFirst);
            statistics.setFirstBidPrice(firstMaxBidOnePrice);
            statistics.setFirstBidPriceXrp(maxBidPriceOneFirstHelp);
            statistics.setFirstBidPriecLtc(maxBidPriceOneFirst);
            statistics.setSecondAskPrice(minAskPriceSecond);
            statistics.setSecondBidPrice(maxBidPriceSecond);
            statistics.setFirstRialtoId(firstRialtoId);
            statistics.setSecondRialtoId(secondRialtoId);
            statistics.setCurrencyPairId(currencyPairId);
            statisticsService.addStatistics(statistics);

            if (margeAskTwo <= getPercentOne() && margeAskTwo != -100 && (!context.getBean(BitsaneBuyOrderBuffer.class).isStatus() && !context.getBean(BitsaneSellOrderBuffer.class).isStatus()) && !reverse){
                Buy buy = new Buy();
                buy.setMarge(margeAskTwo);
                buy.setMinAskPriceTwo(minAskPriceSecond);
                buy.setRialtoId(secondRialtoId);
                buy.setPair(secondCurrencyPair);
                buy.setRialto(secondRialto);
                buy.setContext(context);
                Thread tBuyer = new Thread(buy, "buyer");
                tBuyer.start();
                try {
                    synchronized (tBuyer) {
                        logger.info("Thread Buy wait!");
                        tBuyer.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("Thread Buy waited!");
//                context.getBean(ScheduledExecutorService.class).execute(buy);
                reverse = true;
            }else {
                logger.info("Failure <Bitsane> Buy <= " + " margeAskTwo: " + margeAskTwo + " percentOne: " + getPercentOne() + " !reversetwo: " + reverse);
            }
            if (margeABidTwo >= getPercentTwo() && margeABidTwo != 100 && (!context.getBean(BitsaneBuyOrderBuffer.class).isStatus() && !context.getBean(BitsaneSellOrderBuffer.class).isStatus()) && reverse){
                Sell sell = new Sell();
                sell.setMarge(margeABidTwo);
                sell.setMaxBidPriceTwo(maxBidPriceSecond);
                sell.setRialtoId(secondRialtoId);
                sell.setPair(secondCurrencyPair);
                sell.setRialto(secondRialto);
                sell.setContext(context);

                Thread tSeller = new Thread(sell, "seller");
                tSeller.start();
                try {
                    synchronized (tSeller) {
                        logger.info("Thread Sell wait!");
                        tSeller.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("Thread Sell waited!");

//                context.getBean(ScheduledExecutorService.class).execute(sell);
                reverse = false;
            }else {
                logger.info("Failure <Bitsane> Sell >= " + " margeABidTwo: " + margeABidTwo + " percentTwo: " + getPercentTwo() + " reversetwo: " + reverse);
            }

        } catch (IOException e) {
                    logger.error("Ошибка в запросе API", e);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Ошибка в запросе API").append("\n");
                    stringBuilder.append(e.getMessage()).append("\n");
                    stringBuilder.append(e.getLocalizedMessage()).append("\n");
                    context.getBean(Telegram.class).sendMessage(stringBuilder.toString());
        }catch (Exception e) {
                    logger.error("Неизвестная ошибка в потоке", e);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Неизвестная ошибка в потоке").append("\n");
                    stringBuilder.append(e.getMessage()).append("\n");
                    stringBuilder.append(e.getLocalizedMessage()).append("\n");
                    context.getBean(Telegram.class).sendMessage(stringBuilder.toString());
        }
            }

    }

    public ApiService getFirstRialto() {
        return firstRialto;
    }

    public void setFirstRialto(ApiService firstRialto) {
        this.firstRialto = firstRialto;
    }

    public ApiService getFirstRialtoHelp() {
        return firstRialtoHelp;
    }

    public void setFirstRialtoHelp(ApiService firstRialtoHelp) {
        this.firstRialtoHelp = firstRialtoHelp;
    }

    public ApiService getSecondRialto() {
        return secondRialto;
    }

    public void setSecondRialto(ApiService secondRialto) {
        this.secondRialto = secondRialto;
    }

    public String getFirstCurrencyPairOne() {
        return firstCurrencyPairOne;
    }

    public void setFirstCurrencyPairOne(String firstCurrencyPairOne) {
        this.firstCurrencyPairOne = firstCurrencyPairOne;
    }

    public String getFirstCurrencyPairTwo() {
        return firstCurrencyPairTwo;
    }

    public void setFirstCurrencyPairTwo(String firstCurrencyPairTwo) {
        this.firstCurrencyPairTwo = firstCurrencyPairTwo;
    }

    public String getSecondCurrencyPair() {
        return secondCurrencyPair;
    }

    public void setSecondCurrencyPair(String secondCurrencyPair) {
        this.secondCurrencyPair = secondCurrencyPair;
    }

    public synchronized double getPercentOne() {
        return percentOne;
    }

    public synchronized void setPercentOne(double percentOne) {
        this.percentOne = percentOne;
    }

    public synchronized double getPercentTwo() {
        return percentTwo;
    }

    public synchronized void setPercentTwo(double percentTwo) {
        this.percentTwo = percentTwo;
    }

    public int getFirstRialtoId() {
        return firstRialtoId;
    }

    public void setFirstRialtoId(int firstRialtoId) {
        this.firstRialtoId = firstRialtoId;
    }

    public int getSecodRialtoId() {
        return secondRialtoId;
    }

    public void setSecodRialtoId(int secodRialtoId) {
        this.secondRialtoId = secodRialtoId;
    }

    public int getCurrencyPairId() {
        return currencyPairId;
    }

    public void setCurrencyPairId(int currencyPairId) {
        this.currencyPairId = currencyPairId;
    }

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public synchronized Boolean isFlag() {
        return flag;
    }

    public synchronized void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }
}
