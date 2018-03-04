package ru.leon4uk.app.bot;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.leon4uk.app.bot.impl.ComplexCollector;
import ru.leon4uk.coins.service.ApiService;
import ru.leon4uk.coins.service.binance.api.BinanceApi;
import ru.leon4uk.coins.service.bitfinex.api.BitfinexApi;
import ru.leon4uk.coins.service.bitsane.api.BitsaneApi;
import ru.leon4uk.coins.service.kraken.api.KrakenApi;
import ru.leon4uk.coins.service.poloniex.api.PoloniexApi;
import ru.leon4uk.coins.service.wex.api.WexApi;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Component
public class BotManager implements BotApplication{

    private final static Logger logger = Logger.getLogger(BotManager.class);

    private ApplicationContext context;

    public BotManager() {
    }

    @Override
    public void newComplexCollectorExecuter(String firstRialto, String secondRialto, String firstCurrencyPairOne, String firstCurrencyPairTwo, String secondCurrencyPair) {

      ApiService firtRialtoS;
      ApiService firtRialtoHelpS;
      ApiService secondRialtoS;
        try {
            firtRialtoS = getRialto(firstRialto);
            firtRialtoHelpS = getRialto(firstRialto);
            secondRialtoS = getRialto(secondRialto);

            ComplexCollector complexCollector = context.getBean(ComplexCollector.class);
            complexCollector.setFirstRialto(firtRialtoS);
            complexCollector.setFirstRialtoHelp(firtRialtoHelpS);
            complexCollector.setSecondRialto(secondRialtoS);
            complexCollector.setFirstCurrencyPairOne(firstCurrencyPairOne);
            complexCollector.setFirstCurrencyPairTwo(firstCurrencyPairTwo);
            complexCollector.setSecondCurrencyPair(secondCurrencyPair);
            logger.info("Начинаем работу с биржами " + firstRialto + " " + secondRialto);
            context.getBean(ScheduledExecutorService.class).scheduleAtFixedRate(complexCollector, 10, 10, TimeUnit.SECONDS);

        } catch (Exception e) {
            logger.error("In bot application rialto entity not found");
        }

    }

    private ApiService getRialto(String rialto) throws Exception {
        switch (rialto){
            case "Poloniex":
                return new PoloniexApi("https://poloniex.com/public?command=");
            case "Wex":
                return new WexApi("https://wex.nz/api/3/");
            case "Binance":
                return new BinanceApi("https://www.binance.com/api/v1/");
            case "Kraken":
                return new KrakenApi("https://api.kraken.com/0/");
            case "Bitfinex":
                return new BitfinexApi("https://api.bitfinex.com/v1/");
            case "Bitsane":
                return new BitsaneApi("https://bitsane.com");
            default:
                throw new Exception("rialto not found: " + rialto);
        }
    }

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }
}