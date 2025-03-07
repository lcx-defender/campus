package com.lcx.campus.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    @PreAuthorize("hasAnyAuthority('sys:hello:hello')")
    public String hello() {
        return "Hello, Spring Boot!";
    }
}
