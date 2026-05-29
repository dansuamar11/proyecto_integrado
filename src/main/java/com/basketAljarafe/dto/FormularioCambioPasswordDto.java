package com.basketAljarafe.dto;

public class FormularioCambioPasswordDto {

	private String passwordActual;
	private String nuevaPassword;
	private String confirmarNuevaPassword;

	public String getPasswordActual() {
		return passwordActual;
	}

	public void setPasswordActual(String passwordActual) {
		this.passwordActual = passwordActual;
	}

	public String getNuevaPassword() {
		return nuevaPassword;
	}

	public void setNuevaPassword(String nuevaPassword) {
		this.nuevaPassword = nuevaPassword;
	}

	public String getConfirmarNuevaPassword() {
		return confirmarNuevaPassword;
	}

	public void setConfirmarNuevaPassword(String confirmarNuevaPassword) {
		this.confirmarNuevaPassword = confirmarNuevaPassword;
	}
}
