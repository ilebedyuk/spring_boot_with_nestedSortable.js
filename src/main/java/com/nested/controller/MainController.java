package com.nested.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by elizbor on 01.03.2017.
 */
@Controller
public class MainController {
    @GetMapping("/")
    public String indexPage() {
        return "index";
    }
}
