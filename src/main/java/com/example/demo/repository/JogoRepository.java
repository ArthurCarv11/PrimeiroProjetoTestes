package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Jogo;

public interface JogoRepository extends JpaRepository<Jogo, Long> {

}