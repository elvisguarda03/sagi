package br.com.ternarius.inventario.sagi.domain.enums;

public enum TipoUsuario {
	ADMIN("Administrador"),
	MOD("Moderador"),
	USER("Usuario");

	String tipo;

	TipoUsuario(String tipo) {
		this.tipo = tipo;
	}

    public String getTipo() {return tipo;}
}