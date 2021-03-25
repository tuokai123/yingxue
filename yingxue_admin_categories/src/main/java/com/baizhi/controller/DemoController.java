package com.baizhi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("categories/demo")
    public String demo(){
        System.out.println("categories ....");
        return "categories ok";
    }
}
