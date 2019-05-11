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

	public void cadastrar(Laboratorio laboratorio) {
		repository.save(laboratorio);
	}

	public void update(Laboratorio laboratorio) {
		repository.save(laboratorio);
	}
}