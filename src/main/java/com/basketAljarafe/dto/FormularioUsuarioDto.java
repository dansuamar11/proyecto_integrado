package com.basketAljarafe.dto;

public class FormularioUsuarioDto {

	private String nombreUsuario;
	private String username;
	private String password;
	private String nombreRol;

	// Metodo que sirve para obtener el nombre del usuario.
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	// Metodo que sirve para establecer el nombre del usuario.
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	// Metodo que sirve para obtener el identificador de acceso.
	public String getUsername() {
		return username;
	}

	// Metodo que sirve para establecer el identificador de acceso.
	public void setUsername(String username) {
		this.username = username;
	}

	// Metodo que sirve para obtener la contrasena en texto plano.
	public String getPassword() {
		return password;
	}

	// Metodo que sirve para establecer la contrasena en texto plano.
	public void setPassword(String password) {
		this.password = password;
	}

	// Metodo que sirve para obtener el rol del usuario.
	public String getNombreRol() {
		return nombreRol;
	}

	// Metodo que sirve para establecer el rol del usuario.
	public void setNombreRol(String nombreRol) {
		this.nombreRol = nombreRol;
	}
}
