package ru.leon4uk.app.bot.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.leon4uk.app.bot.impl.polo.BuyPolo;
import ru.leon4uk.app.bot.impl.polo.SellPolo;
import ru.leon4uk.app.bot.telegram.Telegram;
import ru.leon4uk.app.domain.Statistics;
import ru.leon4uk.app.service.StatisticsService;
import ru.leon4uk.coins.service.ApiService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class PoloExecutor implements Runnable{

    @Autowired
    private StatisticsService statisticsService;

    private ApplicationContext context;

    private boolean reverse;
    private final static Logger logger = Logger.getLogger(ComplexCollector.class);
    private double percentOne = -0.26;
    private double percentTwo = 0.35;
    private ApiService firstRialto;
    private ApiService secondRialto;
    private String firstCurrencyPairOne;
    private String secondCurrencyPair;
    private int firstRialtoId;
    private int secondRialtoId;
    private Boolean flag;
    private int currencyPairId;
    private Future<?> future;


    @Override
    public void run() {
        if (this.getFlag().equals(Boolean.TRUE)) {
            synchronized (this.getFuture()) {
                logger.info("make notify!");
                this.getFuture().cancel(true);
                this.getFuture().notifyAll();
                logger.info("notified!");
            }
        }else {
            try{
                Statistics statistics = new Statistics();
                List<Double> firstAskPrices = firstRialto.getAskPrices(firstCurrencyPairOne).stream().map(Double::valueOf).collect(Collectors.toList());
                List<Double> secondAskPrices = secondRialto.getAskPrices(secondCurrencyPair).stream().map(Double::valueOf).collect(Collectors.toList());

                List<Double> firstBidPrices = firstRialto.getBidPrices(firstCurrencyPairOne).stream().map(Double::valueOf).collect(Collectors.toList());
                List<Double> secondBidPrices = secondRialto.getBidPrices(secondCurrencyPair).stream().map(Double::valueOf).collect(Collectors.toList());

                double minAskPriceOneFirst = Collections.min(firstAskPrices);
                double maxBidPriceOneFirst = Collections.max(firstBidPrices);

                double minAskPriceSecond = Collections.min(secondAskPrices);
                double maxBidPriceSecond = Collections.max(secondBidPrices);

                double margeAskOne = (minAskPriceOneFirst - maxBidPriceSecond)/minAskPriceOneFirst*100;
                double margeBidOne = (maxBidPriceOneFirst - minAskPriceSecond)/maxBidPriceOneFirst*100;
                double margeAskTwo = (minAskPriceSecond - maxBidPriceOneFirst)/minAskPriceSecond*100;
                double margeABidTwo = (maxBidPriceSecond - minAskPriceOneFirst)/maxBidPriceSecond*100;

                statistics.setAskMargeOne(margeAskOne);
                statistics.setAskMargeTwo(margeAskTwo);
                statistics.setBidMargeOne(margeBidOne);
                statistics.setBidMargeTwo(margeABidTwo);
                statistics.setFirstAskPrice(minAskPriceOneFirst);
                statistics.setFirstAskPriceXrp(minAskPriceOneFirst);
                statistics.setFirstAskPrieLtc(minAskPriceOneFirst);
                statistics.setFirstBidPrice(maxBidPriceOneFirst);
                statistics.setFirstBidPriceXrp(maxBidPriceOneFirst);
                statistics.setFirstBidPriecLtc(maxBidPriceOneFirst);
                statistics.setSecondAskPrice(minAskPriceSecond);
                statistics.setSecondBidPrice(maxBidPriceSecond);
                statistics.setFirstRialtoId(firstRialtoId);
                statistics.setSecondRialtoId(secondRialtoId);
                statistics.setCurrencyPairId(currencyPairId);
                statisticsService.addStatistics(statistics);

                if (margeAskOne <= getPercentOne() && margeAskTwo != -100 && !reverse){
                    BuyPolo buy = new BuyPolo();
                    buy.setMarge(margeAskOne);
                    buy.setMinAskPrice(minAskPriceOneFirst);
                    buy.setRialtoId(firstRialtoId);
                    buy.setPair(firstCurrencyPairOne);
                    buy.setRialto(firstRialto);
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
                    reverse = true;
                }else {
                    logger.info("Failure <Poloniex> Buy <= " + " margeAskOne: " + margeAskOne + " percentOne: " + getPercentOne() + " !reversetwo: " + reverse);
                }

                if (margeBidOne >= getPercentTwo() && margeABidTwo != 100 && reverse){
                    SellPolo sell = new SellPolo();
                    sell.setMarge(margeBidOne);
                    sell.setMaxBidPriceTwo(maxBidPriceOneFirst);
                    sell.setRialtoId(firstRialtoId);
                    sell.setPair(firstCurrencyPairOne);
                    sell.setRialto(firstRialto);
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

                    reverse = false;
                }else {
                    logger.info("Failure <Poloniex> Sell >= " + " margeBidOne: " + margeBidOne + " percentTwo: " + getPercentTwo() + " reversetwo: " + reverse);
                }
            }catch (IOException e) {
                logger.error("Ошибка в запросе API Poloniex/Binance", e);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Ошибка в запросе API API Poloniex/Binance").append("\n");
                stringBuilder.append(e.getMessage()).append("\n");
                context.getBean(Telegram.class).sendMessage(stringBuilder.toString());
            }catch (Exception ex) {
                logger.error("Неизвестная ошибка в потоке", ex);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Неизвестная ошибка в потоке").append("\n");
                stringBuilder.append(ex.getMessage()).append("\n");
                context.getBean(Telegram.class).sendMessage(stringBuilder.toString());
            }
        }

    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
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

    public ApiService getFirstRialto() {
        return firstRialto;
    }

    public void setFirstRialto(ApiService firstRialto) {
        this.firstRialto = firstRialto;
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

    public String getSecondCurrencyPair() {
        return secondCurrencyPair;
    }

    public void setSecondCurrencyPair(String secondCurrencyPair) {
        this.secondCurrencyPair = secondCurrencyPair;
    }

    public int getFirstRialtoId() {
        return firstRialtoId;
    }

    public void setFirstRialtoId(int firstRialtoId) {
        this.firstRialtoId = firstRialtoId;
    }

    public int getSecondRialtoId() {
        return secondRialtoId;
    }

    public void setSecondRialtoId(int secondRialtoId) {
        this.secondRialtoId = secondRialtoId;
    }

    public synchronized Boolean getFlag() {
        return flag;
    }

    public synchronized void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getCurrencyPairId() {
        return currencyPairId;
    }

    public void setCurrencyPairId(int currencyPairId) {
        this.currencyPairId = currencyPairId;
    }

    public Future<?> getFuture() {
        return future;
    }

    public void setFuture(Future<?> future) {
        this.future = future;
    }

    public StatisticsService getStatisticsService() {
        return statisticsService;
    }

    public void setStatisticsService(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }
}
