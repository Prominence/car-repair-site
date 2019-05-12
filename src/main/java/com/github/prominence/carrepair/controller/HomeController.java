package com.github.prominence.carrepair.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    private static final Logger logger = LogManager.getLogger(HomeController.class);

    @RequestMapping
    public String index() {
        logger.trace("Request to open home page.");
        return "index";
    }
}
