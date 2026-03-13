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

        Usuario usuario = repository.findById(username).get()
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if(!usuario.getPassword().equals(password)){
            throw new RuntimeException("Senha inválida");
        }

        return jwtService.gerarToken(username);
    }

}