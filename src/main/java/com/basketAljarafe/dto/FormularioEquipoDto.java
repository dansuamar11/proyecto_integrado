package com.basketAljarafe.dto;

public class FormularioEquipoDto {

	private String nombre;
	private Integer idEntrenador;

	// Metodo que sirve para obtener el nombre del equipo.
	public String getNombre() {
		return nombre;
	}

	// Metodo que sirve para establecer el nombre del equipo.
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	// Metodo que sirve para obtener el identificador del entrenador.
	public Integer getIdEntrenador() {
		return idEntrenador;
	}

	// Metodo que sirve para establecer el identificador del entrenador.
	public void setIdEntrenador(Integer idEntrenador) {
		this.idEntrenador = idEntrenador;
	}
}
