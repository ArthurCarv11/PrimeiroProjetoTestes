package com.example.demo.security;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // classe de configuração do Spring. O Spring a usará para definir beans e configurações para o contexto da aplicação.
public class SecurityConfig {

    private final JwtFilter jwtFilter; // variavel do tipo JwtFilter que irá validar o token

    public SecurityConfig(JwtFilter jwtFilter) {  // construtor da classe que recebe um JwtFilter como parâmetro e o atribui à variável jwtFilter
        this.jwtFilter = jwtFilter; 
    }

    @Bean 
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
        .csrf(csrf -> csrf.disable()) //desabilita a proteção contra CSRF, que é uma medida de segurança para evitar 
        //ataques de falsificação de solicitação entre sites. No entanto, em APIs RESTful, geralmente não é necessário, 
        //pois as APIs são projetadas para serem stateless e não mantêm sessões.
        

        .sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //configura o gerenciamento de sessões para
            // ser stateless, o que significa que a aplicação não manterá estado de sessão entre as requisições (não vai
            // guardar nada em servidor). 
        )
        
        .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
        ) // configura as regras de autorização para as requisições HTTP. Neste caso, todas as requisições para endpoints
        //que começam na rota "/auth/" são permitidas sem autenticação, enquanto todas as outras requisições exigem 
        //autenticação.
        

        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); //adiciona o filtro 
        //jwtFilter antes do filtro de autenticação padrão do Spring Security
        //que é o UsernamePasswordAuthenticationFilter.
        
        return http.build(); //constrói e retorna a cadeia de filtros de segurança configurada.
    }

}