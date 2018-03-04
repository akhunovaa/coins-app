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
    private int firstrialtoId;
    private int secondrialtoId;

    public BotManager() {
    }

    @Override
    public void newComplexCollectorExecuter(int firstRialto, int secondRialto, String firstCurrencyPairOne, String firstCurrencyPairTwo, String secondCurrencyPair, int currencyPairId) {

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
            complexCollector.setFirstRialtoId(firstRialto);
            complexCollector.setSecodRialtoId(secondRialto);
            logger.info("Начинаем работу с биржами " + firstRialto + " " + secondRialto);
            context.getBean(ScheduledExecutorService.class).scheduleAtFixedRate(complexCollector, 10, 10, TimeUnit.SECONDS);

        } catch (Exception e) {
            logger.error("In bot application rialto entity not found");
        }

    }

    private ApiService getRialto(int rialto) throws Exception {
        switch (rialto){
            case 2:
                return new PoloniexApi("https://poloniex.com/public?command=");
            case 1:
                return new WexApi("https://wex.nz/api/3/");
            case 5:
                return new BinanceApi("https://www.binance.com/api/v1/");
            case 3:
                return new KrakenApi("https://api.kraken.com/0/");
            case 4:
                return new BitfinexApi("https://api.bitfinex.com/v1/");
            case 6:
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