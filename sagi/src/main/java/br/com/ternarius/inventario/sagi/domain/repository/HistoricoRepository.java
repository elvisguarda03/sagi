package br.com.ternarius.inventario.sagi.domain.repository;

import java.util.List;
import java.util.Optional;

import br.com.ternarius.inventario.sagi.domain.entity.Historico;

public interface HistoricoRepository {
	Optional<Historico> findById(String id);
	Historico save(Historico h);
	void delete(Historico h);
	List<Historico> findAll();
}
