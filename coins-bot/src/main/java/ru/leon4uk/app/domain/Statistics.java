package ru.leon4uk.app.domain;


import javax.persistence.*;

@Entity
@Table(name = "statistic_info")
public class Statistics {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_ask_price")
    private Double firstAskPrice;

    @Column(name = "second_ask_price")
    private Double secondAskPrice;

    @Column(name = "first_bid_price")
    private Double firstBidPrice;

    @Column(name = "second_bid_price")
    private Double secondBidPrice;

    @Column(name = "first_ask_price_ltc")
    private Double firstAskPrieLtc;

    @Column(name = "first_bid_price_ltc")
    private Double firstBidPriecLtc;

    @Column(name = "first_ask_price_xrp")
    private Double firstAskPriceXrp;

    @Column(name = "first_bid_price_xrp")
    private Double firstBidPriceXrp;

    @Column(name = "ask_marge_one")
    private Double askMargeOne;

    @Column(name = "bid_marge_one")
    private Double bidMargeOne;

    @Column(name = "ask_marge_two")
    private Double askMargeTwo;

    @Column(name = "bid_marge_two")
    private Double bidMargeTwo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getFirstAskPrice() {
        return firstAskPrice;
    }

    public void setFirstAskPrice(Double firstAskPrice) {
        this.firstAskPrice = firstAskPrice;
    }

    public Double getSecondAskPrice() {
        return secondAskPrice;
    }

    public void setSecondAskPrice(Double secondAskPrice) {
        this.secondAskPrice = secondAskPrice;
    }

    public Double getFirstBidPrice() {
        return firstBidPrice;
    }

    public void setFirstBidPrice(Double firstBidPrice) {
        this.firstBidPrice = firstBidPrice;
    }

    public Double getSecondBidPrice() {
        return secondBidPrice;
    }

    public void setSecondBidPrice(Double secondBidPrice) {
        this.secondBidPrice = secondBidPrice;
    }

    public Double getFirstAskPrieLtc() {
        return firstAskPrieLtc;
    }

    public void setFirstAskPrieLtc(Double firstAskPrieLtc) {
        this.firstAskPrieLtc = firstAskPrieLtc;
    }

    public Double getFirstBidPriecLtc() {
        return firstBidPriecLtc;
    }

    public void setFirstBidPriecLtc(Double firstBidPriecLtc) {
        this.firstBidPriecLtc = firstBidPriecLtc;
    }

    public Double getFirstAskPriceXrp() {
        return firstAskPriceXrp;
    }

    public void setFirstAskPriceXrp(Double firstAskPriceXrp) {
        this.firstAskPriceXrp = firstAskPriceXrp;
    }

    public Double getFirstBidPriceXrp() {
        return firstBidPriceXrp;
    }

    public void setFirstBidPriceXrp(Double firstBidPriceXrp) {
        this.firstBidPriceXrp = firstBidPriceXrp;
    }

    public Double getAskMargeOne() {
        return askMargeOne;
    }

    public void setAskMargeOne(Double askMargeOne) {
        this.askMargeOne = askMargeOne;
    }

    public Double getBidMargeOne() {
        return bidMargeOne;
    }

    public void setBidMargeOne(Double bidMargeOne) {
        this.bidMargeOne = bidMargeOne;
    }

    public Double getAskMargeTwo() {
        return askMargeTwo;
    }

    public void setAskMargeTwo(Double askMargeTwo) {
        this.askMargeTwo = askMargeTwo;
    }

    public Double getBidMargeTwo() {
        return bidMargeTwo;
    }

    public void setBidMargeTwo(Double bidMargeTwo) {
        this.bidMargeTwo = bidMargeTwo;
    }
}
