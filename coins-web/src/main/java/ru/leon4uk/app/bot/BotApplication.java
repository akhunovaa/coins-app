package ru.leon4uk.app.bot;



public interface BotApplication {

    void newComplexCollectorExecuter(int firstRialto, int secondRialto, String firstCurrencyPairOne, String firstCurrencyPairTwo, String secondCurrencyPair, int currencyPairId);

    void orderCancel(String orderId);

    String orderInfo(String orderId);

    void paramsEdit(Double min, Double max);

    void botsStop();

    String[] getParams();

}
