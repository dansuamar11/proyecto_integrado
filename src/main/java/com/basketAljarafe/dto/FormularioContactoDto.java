package com.basketAljarafe.dto;

public class FormularioContactoDto {

	private String nombreContacto;
	private String correoContacto;
	private String telefonoContacto;
	private String mensaje;

	// Metodo que sirve para obtener el nombre del contacto.
	public String getNombreContacto() {
		return nombreContacto;
	}

	// Metodo que sirve para establecer el nombre del contacto.
	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}

	// Metodo que sirve para obtener el correo del contacto.
	public String getCorreoContacto() {
		return correoContacto;
	}

	// Metodo que sirve para establecer el correo del contacto.
	public void setCorreoContacto(String correoContacto) {
		this.correoContacto = correoContacto;
	}

	// Metodo que sirve para obtener el telefono del contacto.
	public String getTelefonoContacto() {
		return telefonoContacto;
	}

	// Metodo que sirve para establecer el telefono del contacto.
	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
	}

	// Metodo que sirve para obtener el mensaje del contacto.
	public String getMensaje() {
		return mensaje;
	}

	// Metodo que sirve para establecer el mensaje del contacto.
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
