package ru.leon4uk.app.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.leon4uk.app.service.CurrencyPairService;

@Component
@Scope("prototype")
public class BotApplication implements Runnable{

    private final static Logger logger = Logger.getLogger(BotApplication.class);
    private ObjectMapper objectMapper;
    private String currencyPair;
    private int firstRialto;
    private int secondRialto;

    @Autowired
    private CurrencyPairService currencyPairService;

    public BotApplication() {
    }

    public void run() {
        currencyPairService.listCurrencyPair().forEach(currencyPair -> logger.info(currencyPair.getPairValue()));
    }

    public static Logger getLogger() {
        return logger;
    }
}
