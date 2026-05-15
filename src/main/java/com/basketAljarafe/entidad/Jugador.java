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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "JUGADOR", uniqueConstraints = {
		@UniqueConstraint(name = "uk_jugador_equipo_dorsal", columnNames = { "id_equipo", "dorsal" })
})
@JsonIgnoreProperties({ "estadisticas" })
public class Jugador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_jugador")
	private Integer idJugador;

	@Column(name = "nombre", nullable = false, length = 50)
	private String nombre;

	@Column(name = "apellidos", nullable = false, length = 100)
	private String apellidos;

	@Column(name = "dorsal")
	private Integer dorsal;

	@Column(name = "activo", nullable = false)
	private boolean activo = true;

	// Conexion que sirve para relacionar un jugador con su equipo.
	@ManyToOne
	@JoinColumn(name = "id_equipo", nullable = false)
	private Equipo equipo;

	// Conexion que sirve para relacionar un jugador con sus estadisticas por partido.
	@OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EstadisticaPartido> estadisticas = new ArrayList<>();

	// Metodo que sirve para crear un jugador vacio.
	public Jugador() {
	}

	// Metodo que sirve para crear un jugador con sus datos principales.
	public Jugador(String nombre, String apellidos, Equipo equipo) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.equipo = equipo;
	}

	// Metodo que sirve para obtener el identificador del jugador.
	public Integer getIdJugador() {
		return idJugador;
	}

	// Metodo que sirve para establecer el identificador del jugador.
	public void setIdJugador(Integer idJugador) {
		this.idJugador = idJugador;
	}

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

	// Metodo que sirve para comprobar si el jugador esta activo.
	public boolean isActivo() {
		return activo;
	}

	// Metodo que sirve para establecer el estado activo del jugador.
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	// Metodo que sirve para obtener el equipo del jugador.
	public Equipo getEquipo() {
		return equipo;
	}

	// Metodo que sirve para establecer el equipo del jugador.
	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}

	// Metodo que sirve para obtener las estadisticas del jugador.
	public List<EstadisticaPartido> getEstadisticas() {
		return estadisticas;
	}

	// Metodo que sirve para establecer las estadisticas del jugador.
	public void setEstadisticas(List<EstadisticaPartido> estadisticas) {
		this.estadisticas = estadisticas;
	}
}
