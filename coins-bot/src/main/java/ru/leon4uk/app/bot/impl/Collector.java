package ru.leon4uk.app.bot.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.leon4uk.app.service.CurrencyPairService;

@Component
@Scope("prototype")
public class Collector implements Runnable{

    @Autowired
    private CurrencyPairService currencyPairService;

    private final static Logger logger = Logger.getLogger(Collector.class);
    private ObjectMapper objectMapper;

    @Override
    public void run() {

    }
}
