package br.com.ternarius.inventario.sagi.infrastructure.service;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.stereotype.Service;

import br.com.ternarius.inventario.sagi.domain.entity.Equipamento;
import br.com.ternarius.inventario.sagi.domain.entity.Historico;

@Service
public class HistoricoListener {
	
	@PrePersist
	@PreUpdate
	public void onChange(Equipamento equipamento) {
		Historico historico = Historico.builder()
				.equipamento(equipamento)
				.build();
		equipamento.addHistorico(historico);
	}
}