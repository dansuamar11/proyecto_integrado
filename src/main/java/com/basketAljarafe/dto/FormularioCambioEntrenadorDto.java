package com.basketAljarafe.dto;

public class FormularioCambioEntrenadorDto {

	private Integer idEquipo;
	private Integer idEntrenador;

	// Metodo que sirve para obtener el identificador del equipo.
	public Integer getIdEquipo() {
		return idEquipo;
	}

	// Metodo que sirve para establecer el identificador del equipo.
	public void setIdEquipo(Integer idEquipo) {
		this.idEquipo = idEquipo;
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
