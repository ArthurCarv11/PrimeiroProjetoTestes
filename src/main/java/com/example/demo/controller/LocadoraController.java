package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.Locadoraservice;
import com.example.demo.model.*;

@RestController
@RequestMapping("/locadora")
public class LocadoraController {

    @Autowired
    private Locadoraservice service;

    @PostMapping("/cliente")
    public Cliente cadastrarCliente(@RequestBody Cliente cliente){
        return service.cadastrarCliente(cliente);
    }

    @PostMapping("/jogo")
    public Jogo adicionarJogo(@RequestBody Jogo jogo){
        return service.adicionarJogo(jogo);
    }

    @DeleteMapping("/jogo/{id}")
    public void removerJogo(@PathVariable Long id){
        service.removerJogo(id);
    }

    @PostMapping("/alugar")
    public Locacao alugarJogo(
            @RequestParam Long clienteId,
            @RequestParam Long jogoId,
            @RequestParam int dias){

        return service.alugarJogo(clienteId, jogoId, dias);
    }
}