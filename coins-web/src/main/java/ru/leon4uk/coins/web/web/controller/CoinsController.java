package ru.leon4uk.coins.web.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
        BotManager botApplication = context.getBean(BotManager.class);
        botApplication.setContext(context);
        String[] params = botApplication.getParams();
        map.put("statistics", new Statistics().getAskMargeOne());
        if (params.length > 1){
            map.put("min", params[0]);
            map.put("max", params[1]);
        }
        map.put("listStatistics", statisticService.listStatistics());
        return "bot";
    }

    @RequestMapping("/bot/add")
    public String botAdd() {
        BotManager botApplication = context.getBean(BotManager.class);
        botApplication.newComplexCollectorExecuter(4, 6, "ltcusd", "xrpusd", "XRP_LTC", 232);
        return "redirect:/bot";
    }

    @RequestMapping("/bot/stop")
    public String botStop() {
        BotManager botApplication = context.getBean(BotManager.class);
        botApplication.botsStop();
        return "redirect:/bot";
    }

    @RequestMapping(value = "/order/cancel", params = {"id"})
    public String orderCancel(@RequestParam("id") String id) {
        BotManager botApplication = context.getBean(BotManager.class);
        botApplication.orderCancel(id);
        return "redirect:/bot";
    }

    @RequestMapping(value = "/bot/params", params = { "min", "max"})
    public String borParamsChange(@RequestParam("min") Double min, @RequestParam("max") Double max) {
        BotManager botApplication = context.getBean(BotManager.class);
        botApplication.paramsEdit(min, max);
        return "redirect:/bot";
    }

    @RequestMapping("/prices")
    public String getPrice(Map<String, Object> map) {
        map.put("statistics", new Statistics().getAskMargeOne());
        map.put("listStatistics", statisticService.listStatistics());
        return "redirect:/bot";
    }

    @RequestMapping(value = "/order/status", params = {"id"})
    public String getOrderInfo(@RequestParam("id") String id, ModelMap model) {
        BotManager botApplication = context.getBean(BotManager.class);
        botApplication.setContext(context);
        String order = botApplication.orderInfo(id);
        model.addAttribute("order", order);
        return "redirect:/bot";
    }

    @RequestMapping(value = "/bot/state", params = {"reverse"})
    public String getOrderInfo(@RequestParam("reverse") String reverse) {
        BotManager botApplication = context.getBean(BotManager.class);
        botApplication.setContext(context);
        botApplication.setReverse(Boolean.parseBoolean(reverse));
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
