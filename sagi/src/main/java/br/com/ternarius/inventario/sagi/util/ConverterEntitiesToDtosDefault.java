package br.com.ternarius.inventario.sagi.util;

import java.util.ArrayList;
import java.util.List;

import br.com.ternarius.inventario.sagi.application.dto.EquipamentoDto;
import br.com.ternarius.inventario.sagi.application.dto.LaboratorioDto;
import br.com.ternarius.inventario.sagi.application.dto.UsuarioDto;
import br.com.ternarius.inventario.sagi.domain.entity.Equipamento;
import br.com.ternarius.inventario.sagi.domain.entity.Laboratorio;
import br.com.ternarius.inventario.sagi.domain.entity.Usuario;

public interface ConverterEntitiesToDtosDefault {
	
	default List<LaboratorioDto> fromLaboratorios(List<Laboratorio> laboratorios) {
		List<LaboratorioDto> dtos = new ArrayList<>();
		laboratorios.forEach(l -> dtos.add(new LaboratorioDto(l)));
	
		return dtos;
	}
	
	default List<EquipamentoDto> fromEquipamentos(List<Equipamento> equipamentos) {
		List<EquipamentoDto> dtos = new ArrayList<>();
		equipamentos.forEach(e -> dtos.add(new EquipamentoDto(e)));
		
		return dtos;
	}
	
	default List<UsuarioDto> fromUsuarios(List<Usuario> usuarios) {
		List<UsuarioDto> dtos = new ArrayList<>();
		usuarios.forEach(u -> dtos.add(new UsuarioDto(u)));
	
		return dtos;
	}
}