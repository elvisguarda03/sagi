package br.com.ternarius.inventario.sagi.domain.service;

import br.com.ternarius.inventario.sagi.domain.entity.SolicitacaoEquipamento;
import br.com.ternarius.inventario.sagi.domain.repository.SolicitacaoEquipamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolicitacaoEquipamentoService {

    private final SolicitacaoEquipamentoRepository solicitacaoEquipamentoRepository;

    public Optional<SolicitacaoEquipamento> findById(String id) {
        return solicitacaoEquipamentoRepository.findById(id);
    }

    public SolicitacaoEquipamento cadastrar(SolicitacaoEquipamento solicitacaoEquipamento) {
        return solicitacaoEquipamentoRepository.save(solicitacaoEquipamento);
    }

    public List<SolicitacaoEquipamento> fimdAll() {
        return solicitacaoEquipamentoRepository.findAll();
    }

    public void deleteById(String id) {
        solicitacaoEquipamentoRepository.deleteById(id);
    }
}
