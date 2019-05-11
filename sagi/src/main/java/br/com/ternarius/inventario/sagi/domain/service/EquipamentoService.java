package br.com.ternarius.inventario.sagi.domain.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
		return equipamentoRepository.save(equipamento);
	}
	
	public List<Equipamento> findAll() {
		return equipamentoRepository.findAll();
	}

	public Page<Equipamento> findAll(Pageable pageable) {
		return equipamentoRepository.findAll(pageable);
	}

	public Optional<Equipamento> findById(String id) {
		return equipamentoRepository.findById(id);
	}

	public void deleteById(String idEquipamento) {
		equipamentoRepository.deleteById(idEquipamento);
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
}