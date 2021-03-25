package com.baizhi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/videos/demo")
    public String demo(){
        System.out.println("videos demo ..");
        return "videos demo";
    }
}
