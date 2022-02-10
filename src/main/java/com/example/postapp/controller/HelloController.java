package com.example.postapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping
    public String getAll () {
        return "helloPage";
    }

    @GetMapping("/api/registration/form")
    public String getRegisterForm() {
        return "html/registerForm";
    }

    @GetMapping("/api/auth/form")
    public String getLoginForm() {
        return "html/authForm";
    }

}
