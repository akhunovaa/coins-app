package ru.leon4uk.coins.app.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.leon4uk.coins.app.web.database.DataBaseImpl;


@Controller
class CoinsController {

    @Autowired
    private DataBaseImpl dataBase;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String listAll(ModelMap model) {

        model.addAttribute("message", 'f');
        return "index";
    }

}
