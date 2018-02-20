package ru.leon4uk.app.bot;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class BotApplication implements ApplicationListener<ContextRefreshedEvent> {

    private final static Logger logger = Logger.getLogger(BotApplication.class);

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.info("hello");
    }
}
