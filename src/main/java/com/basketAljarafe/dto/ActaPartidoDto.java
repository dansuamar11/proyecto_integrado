package com.basketAljarafe.dto;

import java.util.ArrayList;
import java.util.List;

public class ActaPartidoDto {

	private Integer idPartido;
	private String nombreEquipoLocal;
	private String nombreEquipoVisitante;
	private Integer puntosLocal;
	private Integer puntosVisitante;
	private List<EstadisticaJugadorPartidoDto> estadisticasJugadores = new ArrayList<>();

	// Metodo que sirve para obtener el identificador del partido.
	public Integer getIdPartido() {
		return idPartido;
	}

	// Metodo que sirve para establecer el identificador del partido.
	public void setIdPartido(Integer idPartido) {
		this.idPartido = idPartido;
	}

	// Metodo que sirve para obtener el nombre del equipo local.
	public String getNombreEquipoLocal() {
		return nombreEquipoLocal;
	}

	// Metodo que sirve para establecer el nombre del equipo local.
	public void setNombreEquipoLocal(String nombreEquipoLocal) {
		this.nombreEquipoLocal = nombreEquipoLocal;
	}

	// Metodo que sirve para obtener el nombre del equipo visitante.
	public String getNombreEquipoVisitante() {
		return nombreEquipoVisitante;
	}

	// Metodo que sirve para establecer el nombre del equipo visitante.
	public void setNombreEquipoVisitante(String nombreEquipoVisitante) {
		this.nombreEquipoVisitante = nombreEquipoVisitante;
	}

	// Metodo que sirve para obtener los puntos del equipo local.
	public Integer getPuntosLocal() {
		return puntosLocal;
	}

	// Metodo que sirve para establecer los puntos del equipo local.
	public void setPuntosLocal(Integer puntosLocal) {
		this.puntosLocal = puntosLocal;
	}

	// Metodo que sirve para obtener los puntos del equipo visitante.
	public Integer getPuntosVisitante() {
		return puntosVisitante;
	}

	// Metodo que sirve para establecer los puntos del equipo visitante.
	public void setPuntosVisitante(Integer puntosVisitante) {
		this.puntosVisitante = puntosVisitante;
	}

	// Metodo que sirve para obtener la lista de estadisticas de jugadores.
	public List<EstadisticaJugadorPartidoDto> getEstadisticasJugadores() {
		return estadisticasJugadores;
	}

	// Metodo que sirve para establecer la lista de estadisticas de jugadores.
	public void setEstadisticasJugadores(List<EstadisticaJugadorPartidoDto> estadisticasJugadores) {
		this.estadisticasJugadores = estadisticasJugadores;
	}
}
