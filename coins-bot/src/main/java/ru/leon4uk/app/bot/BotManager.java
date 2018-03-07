package ru.leon4uk.app.bot;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.leon4uk.app.bot.impl.ComplexCollector;
import ru.leon4uk.app.bot.impl.buffer.BitsaneBuyOrderBuffer;
import ru.leon4uk.app.bot.impl.buffer.BitsaneSellOrderBuffer;
import ru.leon4uk.coins.service.ApiService;
import ru.leon4uk.coins.service.binance.api.BinanceApi;
import ru.leon4uk.coins.service.bitfinex.api.BitfinexApi;
import ru.leon4uk.coins.service.bitsane.api.BitsaneApi;
import ru.leon4uk.coins.service.kraken.api.KrakenApi;
import ru.leon4uk.coins.service.poloniex.api.PoloniexApi;
import ru.leon4uk.coins.service.wex.api.WexApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Component
public class BotManager implements BotApplication{

    private final static Logger logger = Logger.getLogger(BotManager.class);
    private Map<String, ComplexCollector> tasks = new HashMap<>();

    private ApplicationContext context;
    private ApiService secondRialtoS;

    public BotManager() {
    }

    @Override
    public void newComplexCollectorExecuter(int firstRialto, int secondRialto, String firstCurrencyPairOne, String firstCurrencyPairTwo, String secondCurrencyPair, int currencyPairId) {

      ApiService firtRialtoS;
      ApiService firtRialtoHelpS;

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
            complexCollector.setContext(context);
            complexCollector.setCurrencyPairId(currencyPairId);
            logger.info("Начинаем работу с биржами " + firstRialto + " " + secondRialto);
            Future<?> periodicCollector = context.getBean(ScheduledExecutorService.class).scheduleAtFixedRate(complexCollector, 10, 10, TimeUnit.SECONDS);
            complexCollector.setFuture(periodicCollector);
            complexCollector.setFlag(Boolean.FALSE);
            tasks.put(firstRialto + " " + secondRialto + " " + firstCurrencyPairOne + " " + firstCurrencyPairTwo + " " + secondCurrencyPair + " " + currencyPairId, complexCollector);
        } catch (Exception e) {
            logger.error("In bot application rialto entity not found");
        }

    }



    @Override
    public void orderCancel(String orderId) {
        try {
            secondRialtoS.orderCancel(orderId);
            context.getBean(BitsaneBuyOrderBuffer.class).setStatus(Boolean.FALSE);
            context.getBean(BitsaneSellOrderBuffer.class).setStatus(Boolean.FALSE);
        } catch (IOException e) {
            logger.error("Ошибка отмены ордера", e);
        }
    }

    @Override
    public void botsStop() {
           tasks.forEach((s, complexCollector) -> {
               complexCollector.setFlag(Boolean.TRUE);
               synchronized (complexCollector.getFuture()) {
                   try {
                       complexCollector.getFuture().wait();
                   } catch (InterruptedException e) {
                       logger.error("Ошибка при остановке бота", e);
                   }
           }
        });

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
                return new BitsaneApi("https://bitsane.com", "77F3CFEE9C4462EB987053FB8B467686831C252D3B86A22FA6850193303C91B9", "E290A9111A88C83A93E4AB6FDC6DE37CE98D24CCE765890CDD0EF3F881475AE6");
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