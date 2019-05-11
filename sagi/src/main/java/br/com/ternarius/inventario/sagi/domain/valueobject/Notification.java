package br.com.ternarius.inventario.sagi.domain.valueobject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Elvis da Guarda
 *
 */
public class Notification {
	
	private List<String> errors = new ArrayList<>();
	
	public List<String> getErrors() {
		return Collections.unmodifiableList(this.errors);
	}
	
	public String getFirstError() {
		if (this.errors.size() == 0) {
			return null;
		}
		return this.errors.get(0);
	}
	
	public Notification addError(String error) {
		this.errors.add(error);
		return this;
	}

	public boolean success() {
		return this.errors.size() == 0;
	}
	
	public boolean fail() {
		return !this.success();
	}
}