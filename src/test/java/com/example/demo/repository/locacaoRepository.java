package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Locacao;

public interface locacaoRepository extends JpaRepository<Locacao, Long> {

}