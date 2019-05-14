package br.com.ternarius.inventario.sagi.domain.enums;

public enum Edificio {
	CIMATEC_1("Cimatec 1"),
	CIMATEC_2("Cimatec 2"),
	CIMATEC_3("Cimatec 3"),
	CIMATEC_4("Cimatec 4");
	
	String edificio;
	
	Edificio(String edificio) {
		this.edificio = edificio;
	}
	
	public String getEdificio() {
		return edificio;
	}
}