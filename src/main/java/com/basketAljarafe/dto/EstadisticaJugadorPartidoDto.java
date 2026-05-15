package com.basketAljarafe.dto;

public class EstadisticaJugadorPartidoDto {

	private Integer idJugador;
	private String nombreJugador;
	private String nombreEquipo;
	private Integer dorsal;
	private Integer puntos;
	private Integer asistencias;
	private Integer rebotes;
	private Integer faltas;
	private Integer minutos;

	// Metodo que sirve para obtener el identificador del jugador.
	public Integer getIdJugador() {
		return idJugador;
	}

	// Metodo que sirve para establecer el identificador del jugador.
	public void setIdJugador(Integer idJugador) {
		this.idJugador = idJugador;
	}

	// Metodo que sirve para obtener el nombre visible del jugador.
	public String getNombreJugador() {
		return nombreJugador;
	}

	// Metodo que sirve para establecer el nombre visible del jugador.
	public void setNombreJugador(String nombreJugador) {
		this.nombreJugador = nombreJugador;
	}

	// Metodo que sirve para obtener el nombre del equipo del jugador.
	public String getNombreEquipo() {
		return nombreEquipo;
	}

	// Metodo que sirve para establecer el nombre del equipo del jugador.
	public void setNombreEquipo(String nombreEquipo) {
		this.nombreEquipo = nombreEquipo;
	}

	// Metodo que sirve para obtener el dorsal del jugador.
	public Integer getDorsal() {
		return dorsal;
	}

	// Metodo que sirve para establecer el dorsal del jugador.
	public void setDorsal(Integer dorsal) {
		this.dorsal = dorsal;
	}

	// Metodo que sirve para obtener los puntos del jugador.
	public Integer getPuntos() {
		return puntos;
	}

	// Metodo que sirve para establecer los puntos del jugador.
	public void setPuntos(Integer puntos) {
		this.puntos = puntos;
	}

	// Metodo que sirve para obtener las asistencias del jugador.
	public Integer getAsistencias() {
		return asistencias;
	}

	// Metodo que sirve para establecer las asistencias del jugador.
	public void setAsistencias(Integer asistencias) {
		this.asistencias = asistencias;
	}

	// Metodo que sirve para obtener los rebotes del jugador.
	public Integer getRebotes() {
		return rebotes;
	}

	// Metodo que sirve para establecer los rebotes del jugador.
	public void setRebotes(Integer rebotes) {
		this.rebotes = rebotes;
	}

	// Metodo que sirve para obtener las faltas del jugador.
	public Integer getFaltas() {
		return faltas;
	}

	// Metodo que sirve para establecer las faltas del jugador.
	public void setFaltas(Integer faltas) {
		this.faltas = faltas;
	}

	// Metodo que sirve para obtener los minutos del jugador.
	public Integer getMinutos() {
		return minutos;
	}

	// Metodo que sirve para establecer los minutos del jugador.
	public void setMinutos(Integer minutos) {
		this.minutos = minutos;
	}
}
