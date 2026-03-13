package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service

public class JwtService {

     private final String SECRET = "chave_super_secreta_para_jwt_123456";

    private Key getKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String gerarToken(String username){

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+86400000))
                .signWith(getKey())
                .compact();
    }

    public String extrairUsername(String token){

        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
