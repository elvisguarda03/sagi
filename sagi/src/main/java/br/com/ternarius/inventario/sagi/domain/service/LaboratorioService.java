package br.com.ternarius.inventario.sagi.domain.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.ternarius.inventario.sagi.domain.entity.Laboratorio;
import br.com.ternarius.inventario.sagi.domain.repository.LaboratorioRepository;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Service
@RequiredArgsConstructor
public class LaboratorioService {
	
	private final LaboratorioRepository repository;
		
	public List<Laboratorio> findAll() {
		return repository.findAll();
	}

	public Page<Laboratorio> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Laboratorio cadastrar(Laboratorio laboratorio) {
		return repository.save(laboratorio);
	}

	public Laboratorio update(Laboratorio laboratorio) {
		return repository.save(laboratorio);
	}

	public Boolean existsByLocalizacaoContainingIgnoreCase(String localizacao) {
		return repository.existsByLocalizacaoContainingIgnoreCase(localizacao);
	}

	public Laboratorio findByLocalizacao(String localizacao) {
		return repository.findByLocalizacaoContainingIgnoreCase(localizacao);
	}

    public Boolean existsById(String id) {
    	return repository.existsById(id);
	}

	public void deleteById(String id) {
		repository.deleteById(id);
	}
}