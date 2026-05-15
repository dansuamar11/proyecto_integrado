package com.basketAljarafe.entidad;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "EQUIPO")
@JsonIgnoreProperties({ "jugadores", "partidosComoLocal", "partidosComoVisitante" })
public class Equipo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_equipo")
	private Integer idEquipo;

	@Column(name = "nombre", nullable = false, unique = true, length = 60)
	private String nombre;

	// Conexion que sirve para relacionar un equipo con su entrenador principal.
	@OneToOne
	@JoinColumn(name = "id_entrenador")
	private Usuario entrenador;

	// Conexion que sirve para relacionar un equipo con sus jugadores.
	@OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Jugador> jugadores = new ArrayList<>();

	// Conexion que sirve para relacionar un equipo con los partidos en los que juega como local.
	@OneToMany(mappedBy = "equipoLocal")
	private List<Partido> partidosComoLocal = new ArrayList<>();

	// Conexion que sirve para relacionar un equipo con los partidos en los que juega como visitante.
	@OneToMany(mappedBy = "equipoVisitante")
	private List<Partido> partidosComoVisitante = new ArrayList<>();

	// Metodo que sirve para crear un equipo vacio.
	public Equipo() {
	}

	// Metodo que sirve para crear un equipo con nombre y entrenador.
	public Equipo(String nombre, Usuario entrenador) {
		this.nombre = nombre;
		this.entrenador = entrenador;
	}

	// Metodo que sirve para obtener el identificador del equipo.
	public Integer getIdEquipo() {
		return idEquipo;
	}

	// Metodo que sirve para establecer el identificador del equipo.
	public void setIdEquipo(Integer idEquipo) {
		this.idEquipo = idEquipo;
	}

	// Metodo que sirve para obtener el nombre del equipo.
	public String getNombre() {
		return nombre;
	}

	// Metodo que sirve para establecer el nombre del equipo.
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	// Metodo que sirve para obtener el entrenador del equipo.
	public Usuario getEntrenador() {
		return entrenador;
	}

	// Metodo que sirve para establecer el entrenador del equipo.
	public void setEntrenador(Usuario entrenador) {
		this.entrenador = entrenador;
	}

	// Metodo que sirve para obtener los jugadores del equipo.
	public List<Jugador> getJugadores() {
		return jugadores;
	}

	// Metodo que sirve para establecer los jugadores del equipo.
	public void setJugadores(List<Jugador> jugadores) {
		this.jugadores = jugadores;
	}

	// Metodo que sirve para obtener los partidos como local.
	public List<Partido> getPartidosComoLocal() {
		return partidosComoLocal;
	}

	// Metodo que sirve para establecer los partidos como local.
	public void setPartidosComoLocal(List<Partido> partidosComoLocal) {
		this.partidosComoLocal = partidosComoLocal;
	}

	// Metodo que sirve para obtener los partidos como visitante.
	public List<Partido> getPartidosComoVisitante() {
		return partidosComoVisitante;
	}

	// Metodo que sirve para establecer los partidos como visitante.
	public void setPartidosComoVisitante(List<Partido> partidosComoVisitante) {
		this.partidosComoVisitante = partidosComoVisitante;
	}
}
