package ru.leon4uk.app.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class BotApplication implements Runnable{

    private final static Logger logger = Logger.getLogger(BotApplication.class);
    private ObjectMapper objectMapper;


    public BotApplication(int firstRialto, int secondRialto, String pair) {

    }

    public void run() {

    }
}
