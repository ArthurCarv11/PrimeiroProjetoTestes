package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Dto.LoginRequest;
import com.example.demo.service.Authservice;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private Authservice service;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request){

        return service.login(
            request.getUsername(),
            request.getPassword()
        );
    }

}