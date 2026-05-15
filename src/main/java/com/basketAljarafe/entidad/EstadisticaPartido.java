package com.basketAljarafe.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "ESTADISTICAS_PARTIDO", uniqueConstraints = @UniqueConstraint(columnNames = { "id_jugador", "id_partido" }))
public class EstadisticaPartido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_estadisticas")
	private Integer idEstadisticas;

	// Conexion que sirve para relacionar la estadistica con el jugador correspondiente.
	@ManyToOne
	@JoinColumn(name = "id_jugador", nullable = false)
	private Jugador jugador;

	// Conexion que sirve para relacionar la estadistica con el partido correspondiente.
	@ManyToOne
	@JoinColumn(name = "id_partido", nullable = false)
	private Partido partido;

	@Column(name = "puntos", nullable = false)
	private Integer puntos = 0;

	@Column(name = "asistencias", nullable = false)
	private Integer asistencias = 0;

	@Column(name = "rebotes", nullable = false)
	private Integer rebotes = 0;

	@Column(name = "faltas", nullable = false)
	private Integer faltas = 0;

	@Column(name = "minutos", nullable = false)
	private Integer minutos = 0;

	// Metodo que sirve para crear una estadistica vacia.
	public EstadisticaPartido() {
	}

	// Metodo que sirve para crear una estadistica con sus datos principales.
	public EstadisticaPartido(Jugador jugador, Partido partido, Integer puntos, Integer asistencias, Integer rebotes,
			Integer faltas, Integer minutos) {
		this.jugador = jugador;
		this.partido = partido;
		this.puntos = puntos;
		this.asistencias = asistencias;
		this.rebotes = rebotes;
		this.faltas = faltas;
		this.minutos = minutos;
	}

	// Metodo que sirve para obtener el identificador de la estadistica.
	public Integer getIdEstadisticas() {
		return idEstadisticas;
	}

	// Metodo que sirve para establecer el identificador de la estadistica.
	public void setIdEstadisticas(Integer idEstadisticas) {
		this.idEstadisticas = idEstadisticas;
	}

	// Metodo que sirve para obtener el jugador de la estadistica.
	public Jugador getJugador() {
		return jugador;
	}

	// Metodo que sirve para establecer el jugador de la estadistica.
	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}

	// Metodo que sirve para obtener el partido de la estadistica.
	public Partido getPartido() {
		return partido;
	}

	// Metodo que sirve para establecer el partido de la estadistica.
	public void setPartido(Partido partido) {
		this.partido = partido;
	}

	// Metodo que sirve para obtener los puntos de la estadistica.
	public Integer getPuntos() {
		return puntos;
	}

	// Metodo que sirve para establecer los puntos de la estadistica.
	public void setPuntos(Integer puntos) {
		this.puntos = puntos;
	}

	// Metodo que sirve para obtener las asistencias de la estadistica.
	public Integer getAsistencias() {
		return asistencias;
	}

	// Metodo que sirve para establecer las asistencias de la estadistica.
	public void setAsistencias(Integer asistencias) {
		this.asistencias = asistencias;
	}

	// Metodo que sirve para obtener los rebotes de la estadistica.
	public Integer getRebotes() {
		return rebotes;
	}

	// Metodo que sirve para establecer los rebotes de la estadistica.
	public void setRebotes(Integer rebotes) {
		this.rebotes = rebotes;
	}

	// Metodo que sirve para obtener las faltas de la estadistica.
	public Integer getFaltas() {
		return faltas;
	}

	// Metodo que sirve para establecer las faltas de la estadistica.
	public void setFaltas(Integer faltas) {
		this.faltas = faltas;
	}

	// Metodo que sirve para obtener los minutos de la estadistica.
	public Integer getMinutos() {
		return minutos;
	}

	// Metodo que sirve para establecer los minutos de la estadistica.
	public void setMinutos(Integer minutos) {
		this.minutos = minutos;
	}
}
