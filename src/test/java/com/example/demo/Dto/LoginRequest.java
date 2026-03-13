package com.example.demo.Dto; //Objeto de Transferência de Dados

public class LoginRequest {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
/* essa classe aqui é um transporte de dados entre o JSON e a aplicação
exemplo:
{
  "username": "luiz",
  "password": "123456"
}
ele transforma esse JSON em um Objeto de LoginRequest, que vai pra o sistema
*/