package ru.leon4uk.app.bot.impl;

//import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.leon4uk.coins.service.Api;
import ru.leon4uk.coins.service.RialtoEn;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope("prototype")
public class ComplexCollector implements Runnable{

    private final static Logger logger = Logger.getLogger(ComplexCollector.class);
    private ObjectMapper objectMapper;
    private Api firstRialto;
    private Api secondRialto;
    private RialtoEn firstRialtoEn;
    private RialtoEn secondRialtoEn;
    private String firstCurrencyPairOne;
    private String firstCurrencyPairTwo;
    private String secondCurrencyPair;
    ConcurrentHashMap<String, Double> tradeValues;

    public ComplexCollector() {

    }

    @Override
    public void run() {
        tradeValues = new ConcurrentHashMap<>();
        try {
            RialtoEn firstMainRialtoEn = objectMapper.readValue(firstRialto.getDepthPair(firstCurrencyPairOne), firstRialtoEn.getClass());
            RialtoEn firstHelpRialtoEn = objectMapper.readValue(firstRialto.getDepthPair(firstCurrencyPairTwo), firstRialtoEn.getClass());
            RialtoEn secondMainRialtoEn = objectMapper.readValue(secondRialto.getDepthPair(secondCurrencyPair), secondRialtoEn.getClass());

           logger.info(firstMainRialtoEn);
           logger.info(firstHelpRialtoEn);
           logger.info(secondMainRialtoEn);
        } catch (IOException e) {
            logger.error("Ошибка в запросе API",e);
        }

    }

    public Api getFirstRialto() {
        return firstRialto;
    }

    public void setFirstRialto(Api firstRialto) {
        this.firstRialto = firstRialto;
    }

    public Api getSecondRialto() {
        return secondRialto;
    }

    public void setSecondRialto(Api secondRialto) {
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

    public RialtoEn getFirstRialtoEn() {
        return firstRialtoEn;
    }

    public void setFirstRialtoEn(RialtoEn firstRialtoEn) {
        this.firstRialtoEn = firstRialtoEn;
    }

    public RialtoEn getSecondRialtoEn() {
        return secondRialtoEn;
    }

    public void setSecondRialtoEn(RialtoEn secondRialtoEn) {
        this.secondRialtoEn = secondRialtoEn;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
