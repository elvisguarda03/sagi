package br.com.ternarius.inventario.sagi.domain.enums;
/**
 * 
 * @author elvis
 *
 */

public enum Message {
	MSG_01("Login ou senha inválido."),
	MSG_02("Usuário não cadastrado."),
	MSG_03("As senhas não coincidem."),
	MSG_04("Equipamento não cadastrado."),
	MSG_05("Laboratório foi cadastrado com sucesso."),
	MSG_06("Equipamento cadastrado com sucesso."),
	MSG_07("O usuário deve ser informado."),
	MSG_08("E-mail já cadastrado."),
	MSG_09("E-mail inválido ou inexistente."),
	MSG_10("Token inválido ou expirado."),
	MSG_11("A sua conta foi ativada.\nBem vindo ao Sagi!"),
	MSG_12("Usuário ativado com sucesso."),
	MSG_13("Solicitação excluída com sucesso."),
	MSG_14("A solicitação para validação da sua conta foi enviado para o admin do sistema.");
	
	private String message;
	
	Message(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}