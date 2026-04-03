package com.example.demo.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Cliente;
import com.example.demo.model.Jogo;
import com.example.demo.model.Locacao;
import com.example.demo.repository.JogoRepository;
import com.example.demo.repository.clienteRepository;
import com.example.demo.repository.locacaoRepository;

@Service
public class Locadoraservice {

    @Autowired
    private clienteRepository clienteRepo;

    @Autowired
    private JogoRepository jogoRepo;

    @Autowired
    private locacaoRepository locacaoRepo;

    public Cliente cadastrarCliente(Cliente cliente){
        return clienteRepo.save(cliente);
    }

    public Jogo adicionarJogo(Jogo jogo){
        return jogoRepo.save(jogo);
    }

    public void removerJogo(Long id){
        jogoRepo.deleteById(id);
    }

    public Locacao alugarJogo(Long clienteId, Long jogoId, int dias){

        Cliente cliente = clienteRepo.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        Jogo jogo = jogoRepo.findById(jogoId)
            .orElseThrow(() -> new RuntimeException("Jogo não encontrado"));

        Locacao locacao = new Locacao();

        locacao.setCliente(cliente);
        locacao.setJogo(jogo);

        locacao.setDataLocacao(LocalDate.now());
        locacao.setDataDevolucao(LocalDate.now().plusDays(dias));

        double valor = jogo.getPrecoBase() * dias;

        locacao.setValorFinal(valor);
        locacao.setStatus(1);

        jogo.setQuantidade(jogo.getQuantidade() - 1);

        jogoRepo.save(jogo);

        return locacaoRepo.save(locacao);
    }

    public float calcularMulta(Locacao locacao){

        if(locacao.getDataDevolucaoReal().isAfter(locacao.getDataDevolucao())){

            long atraso = ChronoUnit.DAYS.between(
                    locacao.getDataDevolucao(),
                    locacao.getDataDevolucaoReal());

            return atraso * 10;
        }

        return 0;
    }
}