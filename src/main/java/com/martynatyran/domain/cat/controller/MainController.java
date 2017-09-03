package com.martynatyran.domain.cat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String homepage(){
        return "index";
    }

    @RequestMapping("/cat/cats")
    public String cats(){
        return "cat/cats";
    }
}
