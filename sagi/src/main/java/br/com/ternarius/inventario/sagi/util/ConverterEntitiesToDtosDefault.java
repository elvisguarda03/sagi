package br.com.ternarius.inventario.sagi.util;

import java.util.ArrayList;
import java.util.List;

import br.com.ternarius.inventario.sagi.application.dto.EquipamentoDto;
import br.com.ternarius.inventario.sagi.application.dto.LaboratorioDto;
import br.com.ternarius.inventario.sagi.application.dto.SolicitacaoEquipamentoDto;
import br.com.ternarius.inventario.sagi.application.dto.UsuarioDto;
import br.com.ternarius.inventario.sagi.domain.entity.Equipamento;
import br.com.ternarius.inventario.sagi.domain.entity.Laboratorio;
import br.com.ternarius.inventario.sagi.domain.entity.SolicitacaoEquipamento;
import br.com.ternarius.inventario.sagi.domain.entity.Usuario;

public interface ConverterEntitiesToDtosDefault {
	
	default List<LaboratorioDto> fromLaboratorios(List<Laboratorio> laboratorios) {
		var dtos = new ArrayList<LaboratorioDto>();
		laboratorios.forEach(l -> dtos.add(new LaboratorioDto(l)));
	
		return dtos;
	}
	
	default List<EquipamentoDto> fromEquipamentos(List<Equipamento> equipamentos) {
		var dtos = new ArrayList<EquipamentoDto>();
		equipamentos.forEach(e -> dtos.add(new EquipamentoDto(e)));
		
		return dtos;
	}
	
	default List<UsuarioDto> fromUsuarios(List<Usuario> usuarios) {
		var dtos = new ArrayList<UsuarioDto>();
		usuarios.forEach(u -> dtos.add(new UsuarioDto(u)));
	
		return dtos;
	}

    default List<SolicitacaoEquipamentoDto> fromSolicitacoes(List<SolicitacaoEquipamento> solicitacoes) {
		var dtos = new ArrayList<SolicitacaoEquipamentoDto>();
		solicitacoes.forEach(s -> dtos.add(new SolicitacaoEquipamentoDto(s)));

		return dtos;
	}
}