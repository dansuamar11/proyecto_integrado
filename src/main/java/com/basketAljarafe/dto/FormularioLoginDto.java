package com.basketAljarafe.dto;

public class FormularioLoginDto {

	private String username;
	private String password;

	// Metodo que sirve para obtener el nombre de acceso del usuario.
	public String getUsername() {
		return username;
	}

	// Metodo que sirve para establecer el nombre de acceso del usuario.
	public void setUsername(String username) {
		this.username = username;
	}

	// Metodo que sirve para obtener la contrasena del usuario.
	public String getPassword() {
		return password;
	}

	// Metodo que sirve para establecer la contrasena del usuario.
	public void setPassword(String password) {
		this.password = password;
	}
}
