package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.JwtService;

@Service
public class Authservice {

    @Autowired
    private UsuarioRepository repository; //injeção de dependência do repositório de usuários para acessar os dados dos usuários no banco de dados

    @Autowired
    private JwtService jwtService; //injeção de dependência do serviço de JWT para gerar tokens de autenticação

    @Autowired
    private PasswordEncoder passwordEncoder; //compara a senha enviada pelo usuário com a senha criptografada armazenada no banco de dados

    public String login(String username, String password){ //recebe o username e password do controller

        Usuario usuario = repository.findByUsername(username) 
                .orElseThrow(() -> new RuntimeException("User not found")); //busca o usuário no banco de dados pelo username, se não encontrar, lança uma exceção

        if(!passwordEncoder.matches(password, usuario.getPassword())){ //compara a senha enviada (texto puro) com a senha criptografada no banco — nunca descriptografa, sempre compara nesse sentido
            throw new RuntimeException("Senha inválida");
        }
 
        return jwtService.gerarToken(username); //gera um token JWT usando o serviço de JWT e retorna o token para o controller
    }

    public String cadastrar(String username, String password, String role){

    if(repository.findByUsername(username).isPresent()){
        throw new RuntimeException("Username já existe");
    }

    Usuario usuario = new Usuario(
        username,
        passwordEncoder.encode(password), // criptografa a senha antes de salvar
        role
    );

    repository.save(usuario);
    return "Funcionário cadastrado com sucesso";
}


}