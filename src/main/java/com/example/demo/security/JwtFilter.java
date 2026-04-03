package com.example.demo.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    // Rotas que não precisam de autenticação — o filtro é ignorado nelas
    private static final List<String> PUBLIC_PATHS = List.of("/auth/", "/public/", "/swagger-ui/", "/v3/api-docs/");

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization"); // Pega o header Authorization, onde o token JWT deve ser 
        //enviado no formato "Bearer <token>"

        // Verifica se o header existe e segue o padrão Bearer
        if (header != null && header.startsWith("Bearer ")) {

            // Remove o "Bearer " e pega o token
            String token = header.substring(7);

            try {
                if (jwtService.isTokenValido(token)) {

                    // Extrai o username contido no payload do token
                    String username = jwtService.extrairUsername(token);

                    // Só autentica se tiver username e o contexto ainda não tiver autenticação
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                        // Cria o objeto de autenticação com username, sem senha e sem roles por enquanto
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        username,
                                        null,
                                        Collections.emptyList()
                                );

                        // Registra a autenticação no contexto do Spring Security para essa thread
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }

            } catch (Exception e) {
                // Qualquer erro inesperado: loga e limpa o contexto para não autenticar indevidamente
                logger.warn("Erro inesperado ao processar token JWT: {}", e.getMessage());
                SecurityContextHolder.clearContext();

                // Retorna 401 imediatamente ao invés de continuar com contexto inválido
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        // Passa a requisição para o próximo filtro ou controller da cadeia
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        // Retorna true (pula o filtro) se a rota for pública
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }
}