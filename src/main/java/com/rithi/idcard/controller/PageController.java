package com.rithi.idcard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/profiles")
    public String profilesPage() {
        return "profiles";
    }
}
