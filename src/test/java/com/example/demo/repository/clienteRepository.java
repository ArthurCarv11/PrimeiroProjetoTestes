package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Cliente;

public interface clienteRepository extends JpaRepository<Cliente, Long> {

}