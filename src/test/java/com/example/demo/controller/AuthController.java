package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Dto.LoginRequest; //de onde vem os dados do login request
import com.example.demo.service.Authservice;

@RestController
@RequestMapping("/auth") //Define a rota
public class AuthController {

    @Autowired //injeção de dependência do serviço de autenticação
    private Authservice service;

    @PostMapping("/login") 
    public String login(@RequestBody LoginRequest request){ /*
 O login precisa de username e password, que estão encapsulados no LoginRequest  o @RequestBody 
 indica que esses dados virão do corpo da requisição HTTP, geralmente em formato JSON. O Spring 
 vai automaticamente converter o JSON recebido em um objeto do tipo LoginRequest, q será armazenado
 no parametro "request" e depois é só pegar o username e password do request e passar
 pro serviço de autenticação */
        return service.login(
            request.getUsername(),
            request.getPassword()
        );
    }

}