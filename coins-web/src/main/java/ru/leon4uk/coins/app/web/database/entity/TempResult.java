package ru.leon4uk.coins.app.web.database.entity;

import java.sql.Timestamp;

public class TempResult {

    private int id;
    private int firstRialtoId;
    private int secondRialtoId;
    private Timestamp time;
    private int currencyPairId;
    private int currencyPairIdTwo;
    private double firstBidPrice;
    private double secondBidPrice;
    private double firstBidAmount;
    private double secondBidAmount;
    private double firstAskPrice;
    private double secondAskPrice;
    private double firstAskAmount;
    private double secondAskAmount;
    private double priceDifference;
    private String rialtoNameOne;
    private String rialtoNameTwo;
    private String currencyPairName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getCurrencyPairId() {
        return currencyPairId;
    }

    public void setCurrencyPairId(int currencyPairId) {
        this.currencyPairId = currencyPairId;
    }

    public double getFirstBidPrice() {
        return firstBidPrice;
    }

    public void setFirstBidPrice(double firstBidPrice) {
        this.firstBidPrice = firstBidPrice;
    }

    public double getSecondBidPrice() {
        return secondBidPrice;
    }

    public void setSecondBidPrice(double secondBidPrice) {
        this.secondBidPrice = secondBidPrice;
    }

    public double getFirstBidAmount() {
        return firstBidAmount;
    }

    public void setFirstBidAmount(double firstBidAmount) {
        this.firstBidAmount = firstBidAmount;
    }

    public double getSecondBidAmount() {
        return secondBidAmount;
    }

    public void setSecondBidAmount(double secondBidAmount) {
        this.secondBidAmount = secondBidAmount;
    }

    public double getFirstAskPrice() {
        return firstAskPrice;
    }

    public void setFirstAskPrice(double firstAskPrice) {
        this.firstAskPrice = firstAskPrice;
    }

    public double getSecondAskPrice() {
        return secondAskPrice;
    }

    public void setSecondAskPrice(double secondAskPrice) {
        this.secondAskPrice = secondAskPrice;
    }

    public double getFirstAskAmount() {
        return firstAskAmount;
    }

    public void setFirstAskAmount(double firstAskAmount) {
        this.firstAskAmount = firstAskAmount;
    }

    public double getSecondAskAmount() {
        return secondAskAmount;
    }

    public void setSecondAskAmount(double secondAskAmount) {
        this.secondAskAmount = secondAskAmount;
    }

    public double getPriceDifference() {
        return priceDifference;
    }

    public void setPriceDifference(double priceDifference) {
        this.priceDifference = priceDifference;
    }

    public String getRialtoNameOne() {
        return rialtoNameOne;
    }

    public void setRialtoNameOne(String rialtoNameOne) {
        this.rialtoNameOne = rialtoNameOne;
    }

    public String getRialtoNameTwo() {
        return rialtoNameTwo;
    }

    public void setRialtoNameTwo(String rialtoNameTwo) {
        this.rialtoNameTwo = rialtoNameTwo;
    }

    public String getCurrencyPairName() {
        return currencyPairName;
    }

    public void setCurrencyPairName(String currencyPairName) {
        this.currencyPairName = currencyPairName;
    }

    public int getCurrencyPairIdTwo() {
        return currencyPairIdTwo;
    }

    public void setCurrencyPairIdTwo(int currencyPairIdTwo) {
        this.currencyPairIdTwo = currencyPairIdTwo;
    }
}