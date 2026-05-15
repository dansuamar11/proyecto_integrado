package com.basketAljarafe.entidad;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "PARTIDO")
@JsonIgnoreProperties({ "estadisticas" })
public class Partido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_partido")
	private Integer idPartido;

	// Conexion que sirve para relacionar el partido con el equipo local.
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_equipo_local", nullable = false)
	private Equipo equipoLocal;

	// Conexion que sirve para relacionar el partido con el equipo visitante.
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_equipo_visitante", nullable = false)
	private Equipo equipoVisitante;

	@Column(name = "fecha", nullable = false)
	private LocalDateTime fecha;

	@Column(name = "puntos_local")
	private Integer puntosLocal = 0;

	@Column(name = "puntos_visitante")
	private Integer puntosVisitante = 0;

	// Conexion que sirve para relacionar el partido con el arbitro responsable.
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_arbitro")
	private Usuario arbitro;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", nullable = false, length = 20)
	private EstadoPartido estado = EstadoPartido.PENDIENTE;

	// Conexion que sirve para relacionar un partido con las estadisticas de sus jugadores.
	@OneToMany(mappedBy = "partido", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EstadisticaPartido> estadisticas = new ArrayList<>();

	// Metodo que sirve para crear un partido vacio.
	public Partido() {
	}

	// Metodo que sirve para crear un partido con sus datos principales.
	public Partido(Equipo equipoLocal, Equipo equipoVisitante, LocalDateTime fecha, Usuario arbitro) {
		this.equipoLocal = equipoLocal;
		this.equipoVisitante = equipoVisitante;
		this.fecha = fecha;
		this.arbitro = arbitro;
	}

	// Metodo que sirve para obtener el identificador del partido.
	public Integer getIdPartido() {
		return idPartido;
	}

	// Metodo que sirve para establecer el identificador del partido.
	public void setIdPartido(Integer idPartido) {
		this.idPartido = idPartido;
	}

	// Metodo que sirve para obtener el equipo local.
	public Equipo getEquipoLocal() {
		return equipoLocal;
	}

	// Metodo que sirve para establecer el equipo local.
	public void setEquipoLocal(Equipo equipoLocal) {
		this.equipoLocal = equipoLocal;
	}

	// Metodo que sirve para obtener el equipo visitante.
	public Equipo getEquipoVisitante() {
		return equipoVisitante;
	}

	// Metodo que sirve para establecer el equipo visitante.
	public void setEquipoVisitante(Equipo equipoVisitante) {
		this.equipoVisitante = equipoVisitante;
	}

	// Metodo que sirve para obtener la fecha del partido.
	public LocalDateTime getFecha() {
		return fecha;
	}

	// Metodo que sirve para establecer la fecha del partido.
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
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

	// Metodo que sirve para obtener el arbitro del partido.
	public Usuario getArbitro() {
		return arbitro;
	}

	// Metodo que sirve para establecer el arbitro del partido.
	public void setArbitro(Usuario arbitro) {
		this.arbitro = arbitro;
	}

	// Metodo que sirve para obtener el estado del partido.
	public EstadoPartido getEstado() {
		return estado;
	}

	// Metodo que sirve para establecer el estado del partido.
	public void setEstado(EstadoPartido estado) {
		this.estado = estado;
	}

	// Metodo que sirve para obtener las estadisticas del partido.
	public List<EstadisticaPartido> getEstadisticas() {
		return estadisticas;
	}

	// Metodo que sirve para establecer las estadisticas del partido.
	public void setEstadisticas(List<EstadisticaPartido> estadisticas) {
		this.estadisticas = estadisticas;
	}
}
