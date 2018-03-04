package ru.leon4uk.app.bot.impl;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;;
import ru.leon4uk.coins.service.ApiService;

import java.io.IOException;

@Component
@Scope("prototype")
public class Buy implements Runnable {

    private final static Logger logger = Logger.getLogger(Buy.class);
    private ApiService rialto;
    private double marge;
    private double minAskPriceTwo;
    private int rialtoId;
    private String pair;
    double fee = 0.25;

    public Buy() {
    }

    @Override
    public void run() {
        logger.info("buy");

        double buyPrice = minAskPriceTwo; //находим минимальную цену. за которую продадут XRP. ask ордера на продажу
        try {
            rialto.getBalance();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        double ltcBalanceOrder = ltcBalanceSecond;
//
//        ltcBalanceSecond = ltcBalanceSecond - ltcBalanceOrder; //Минусуем баланс LTC на кол-во выставленного ордера монет
//
//        double xrpBoughtAmount = buy(buyPrice, ltcBalanceOrder, fee);//осуществляем покупку XRP
//
//        dataBaseWorker.balanceUp(xrpBoughtAmount + xrpBalanceSecond, secondRialtoId, xrpId);//обновляем баланс купленного Риппла
//        dataBaseWorker.balanceUp(ltcBalanceSecond, secondRialtoId, ltcId);//обновляем баланс потраченного на покупку Риппла LTC
//        dataBaseWorker.tradeHistoryInsert(2, buyPrice, ltcBalanceOrder, fee, secondRialtoId, currencies[0] + "->" + currencies[1], xrpBoughtAmount, priceDifferenceOld, ltcBalance, xrpBalance, ltcBalanceSecond, xrpBoughtAmount + xrpBalanceSecond, priceDifferenceOld, minAskPriceTwo, 0);
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
}
