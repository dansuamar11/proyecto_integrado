package com.basketAljarafe.dto;

public class FormularioJugadorDto {

	private String nombre;
	private String apellidos;
	private Integer dorsal;

	// Metodo que sirve para obtener el nombre del jugador.
	public String getNombre() {
		return nombre;
	}

	// Metodo que sirve para establecer el nombre del jugador.
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	// Metodo que sirve para obtener los apellidos del jugador.
	public String getApellidos() {
		return apellidos;
	}

	// Metodo que sirve para establecer los apellidos del jugador.
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	// Metodo que sirve para obtener el dorsal del jugador.
	public Integer getDorsal() {
		return dorsal;
	}

	// Metodo que sirve para establecer el dorsal del jugador.
	public void setDorsal(Integer dorsal) {
		this.dorsal = dorsal;
	}
}
