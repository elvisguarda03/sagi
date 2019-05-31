package br.com.ternarius.inventario.sagi.domain.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.ternarius.inventario.sagi.domain.entity.Equipamento;
import br.com.ternarius.inventario.sagi.domain.repository.EquipamentoRepository;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

/**
 *
 * @author Elvis da Guarda
 *
 */

@Service
@RequiredArgsConstructor
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;

    public Equipamento cadastrar(Equipamento equipamento) {
        if (equipamentoRepository.existsByCodigoPatrimonio(equipamento.getCodigoPatrimonio())) {
            equipamento = equipamentoRepository.findByCodigoPatrimonio(equipamento.getCodigoPatrimonio());
            equipamento.setIsDelete(false);

            update(equipamento);

            return equipamento;
        }
        return equipamentoRepository.save(equipamento);
    }

    public Equipamento update(Equipamento equipamento) {
        return equipamentoRepository.save(equipamento);
    }

    public Page<Equipamento> findAll(Pageable pageable) {
        return equipamentoRepository.findAll(pageable);
    }

    public List<Equipamento> findAll() {
        return equipamentoRepository.findAll(false);
    }

    public Optional<Equipamento> findById(String id) {
        return equipamentoRepository.findById(id);
    }

    public List<Equipamento> findByStatus() {
        return equipamentoRepository.findByStatus(true);
    }

    public void unavailable(String idEquipamento) {
        var equipamento = equipamentoRepository.findById(idEquipamento).get();
        equipamento.setIsDelete(true);

        equipamentoRepository.save(equipamento);
    }

    public Boolean existsById(String idEquipamento) {
        return equipamentoRepository.existsById(idEquipamento);
    }

    public Boolean existsByNomeEquipamento(String nomeEquipamento) {
        return equipamentoRepository.existsByNomeEquipamentoContainingIgnoreCase(nomeEquipamento);
    }

    public List<Equipamento> find(String nome, String localizacao, Long codigoPatrimonio, BigDecimal valor, EntityManager em) {
        return equipamentoRepository.find(nome, localizacao, codigoPatrimonio, valor, em);
    }

    public Equipamento updateStatusMaitenance(String id, Boolean isMaintenance) {
        return equipamentoRepository.updateIsMaintenance(id, isMaintenance);
    }

}