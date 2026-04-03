package com.example.demo.security;
 
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
 
import java.security.Key;
import java.util.Date;
 
@Service
public class JwtService {
 
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
 
    // Chave lida do application.properties via variável de ambiente — nunca hardcode aqui
    @Value("${jwt.secret:locadora_chave_secreta_jwt_2024_xpto}")
    private String secret;
 
    // Converte a SECRET string em uma chave criptográfica HMAC-SHA256
    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
 
    // Gera um token JWT com o username no subject, data de emissão e expiração de 24h
    public String gerarToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getKey())
                .compact();
    }
 
    // Extrai o username (subject) contido no payload do token
    public String extrairUsername(String token) {
        return getClaims(token).getSubject();
    }
 
    // Valida o token: verifica assinatura, expiração e presença do username
    public boolean isTokenValido(String token) {
        try {
            Claims claims = getClaims(token); // Parseia o token e pega as claims (payload). Se inválido, lança exceção.
            String username = claims.getSubject(); // Extrai o username do payload
            Date expiracao = claims.getExpiration(); // Extrai a data de expiração do payload
 
            return username != null && !username.isEmpty() // Verifica se o username existe e a data de expiração é válida (não expirou)
                    && expiracao != null && !expiracao.before(new Date());
 
        } catch (ExpiredJwtException e) {
            // Token válido mas já expirou
            logger.warn("Token expirado: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            // Token com formato inválido
            logger.warn("Token malformado: {}", e.getMessage());
            return false;
        }  catch (JwtException e) {
            // Qualquer outro erro relacionado ao JWT
            logger.warn("Erro ao validar token JWT: {}", e.getMessage());
            return false;
        }
    }
 
    // Parseia o token e retorna todas as claims (payload). Lança exceção se inválido
    private Claims getClaims(String token) { 
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
/* O payload é basicamente um JSON com informações sobre o usuário e o token. Cada campo dentro desse JSON se chama claim */