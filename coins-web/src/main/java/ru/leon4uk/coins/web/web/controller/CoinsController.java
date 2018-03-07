package ru.leon4uk.coins.web.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.leon4uk.app.bot.BotManager;
import ru.leon4uk.app.domain.Statistics;
import ru.leon4uk.app.service.CurrencyPairService;
import ru.leon4uk.app.service.StatisticsService;
import ru.leon4uk.coins.web.domain.User;
import ru.leon4uk.coins.web.service.UserService;

import java.util.Map;

@Controller
class CoinsController {

    @Autowired
    private StatisticsService statisticService;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private CurrencyPairService currencyPairService;

    @RequestMapping("/index")
    public String listUser(Map<String, Object> map) {
        map.put("user", new User());
        map.put("listUser", userService.listUser());
        return "user";
    }

    @RequestMapping("/")
    public String home() {
        return "redirect:/bot";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/bot")
    public String bot(Map<String, Object> map) {
        map.put("statistics", new Statistics().getAskMargeOne());
        map.put("listStatistics", statisticService.listStatistics());
        return "bot";
    }

    @RequestMapping("/bot/add")
    public String botAdd() {
        BotManager botApplication = context.getBean(BotManager.class);
        botApplication.setContext(context);
        botApplication.newComplexCollectorExecuter(4, 6, "ltcusd", "xrpusd", "XRP_LTC", 232);
        return "redirect:/bot";
    }

    @RequestMapping("/bot/stop")
    public String botStop() {
        BotManager botApplication = context.getBean(BotManager.class);
        botApplication.setContext(context);
        botApplication.botsStop();
        return "redirect:/bot";
    }

    @RequestMapping("/order/{id}/cancel")
    public String orderCancel(@PathVariable String id) {
        BotManager botApplication = context.getBean(BotManager.class);
        botApplication.setContext(context);
        botApplication.orderCancel(id);
        return "redirect:/bot";
    }

    @RequestMapping(value = "/bot/params", params = { "min", "max"})
    public String borParamsChange(Map<String, Object> map, @RequestParam("min") Double min, @RequestParam("max") Double max) {
        BotManager botApplication = context.getBean(BotManager.class);
        botApplication.setContext(context);
        botApplication.paramsEdit(min, max);
        map.put("paramMin", min);
        map.put("paramMax", max);
        return "redirect:/bot";
    }

    @RequestMapping("/prices")
    public String getPrice(Map<String, Object> map) {
        map.put("statistics", new Statistics().getAskMargeOne());
        map.put("listStatistics", statisticService.listStatistics());
        return "redirect:/bot";
    }

    @RequestMapping("/order/{id}/info")
    public String getOrderInfo(@PathVariable String id, Map<String, Object> map) {
        BotManager botApplication = context.getBean(BotManager.class);
        botApplication.setContext(context);
        String orderInfo = botApplication.orderInfo(id);
        map.put("orderInfo", orderInfo);
        return "redirect:/bot";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addUser(@ModelAttribute("user") User user) {
        userService.addUser(user);
        return "redirect:/index";
    }

    @RequestMapping("/delete/{userId}")
    public String deleteUser(@PathVariable("userId") Integer userId) {
        userService.removeUser(userId);
        return "redirect:/index";
    }

}
