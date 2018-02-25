package ru.leon4uk.coins.app.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.leon4uk.coins.app.domain.User;
import ru.leon4uk.coins.app.service.UserService;

import java.util.Map;


@Controller
class CoinsController {

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String listUser(Map<String, Object> map) {
        map.put("user", new User());
        map.put("listUser", userService.listUser());
        return "user";
    }

    @RequestMapping("/")
    public String home() {
        return "redirect:/index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
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
