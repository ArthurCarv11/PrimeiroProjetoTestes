package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.JwtService;

@Service
public class Authservice {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private JwtService jwtService;

    public String login(String username, String password){ 

        Usuario usuario = repository.findByUsername(username) 
                .orElseThrow(() -> new RuntimeException("User not found")); 

        if(!usuario.getPassword().equals(password)){ //verifica se a senha fornecida é igual a senha do usuário encontrado, se não for, lança uma exceção
            throw new RuntimeException("Senha inválida");
        } //recebe o username e password do controller
 
        return jwtService.gerarToken(username); //gera um token JWT usando o serviço de JWT e retorna o token para o controller
    }

}