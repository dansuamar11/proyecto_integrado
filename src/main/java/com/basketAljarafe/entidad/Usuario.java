package com.basketAljarafe.entidad;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "USUARIO")
@JsonIgnoreProperties({ "passwordHash", "equipoEntrenado", "partidosArbitrados", "solicitudesCreadas" })
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Integer idUsuario;

	@Column(name = "nombre", nullable = false, length = 50)
	private String nombreUsuario;

	@Column(name = "username", nullable = false, unique = true, length = 50)
	private String username;

	@Column(name = "password_hash", nullable = false, length = 255)
	private String passwordHash;

	@Column(name = "activo", nullable = false)
	private boolean activo = true;

	// Conexion que sirve para relacionar cada usuario con un rol.
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_rol_user", nullable = false)
	private Rol rol;

	// Conexion que sirve para relacionar un entrenador con el equipo que dirige.
	@OneToOne(mappedBy = "entrenador")
	private Equipo equipoEntrenado;

	// Conexion que sirve para relacionar un arbitro con los partidos asignados.
	@OneToMany(mappedBy = "arbitro", cascade = CascadeType.ALL)
	private List<Partido> partidosArbitrados = new ArrayList<>();

	// Conexion que sirve para relacionar un usuario con las solicitudes de contacto registradas.
	@OneToMany(mappedBy = "usuarioCreador")
	private List<SolicitudContacto> solicitudesCreadas = new ArrayList<>();

	// Metodo que sirve para crear un usuario vacio.
	public Usuario() {
	}

	// Metodo que sirve para crear un usuario con sus datos principales.
	public Usuario(String nombreUsuario, String username, String passwordHash, Rol rol) {
		this.nombreUsuario = nombreUsuario;
		this.username = username;
		this.passwordHash = passwordHash;
		this.rol = rol;
	}

	// Metodo que sirve para obtener el identificador del usuario.
	public Integer getIdUsuario() {
		return idUsuario;
	}

	// Metodo que sirve para establecer el identificador del usuario.
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	// Metodo que sirve para obtener el nombre del usuario.
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	// Metodo que sirve para establecer el nombre del usuario.
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	// Metodo que sirve para obtener el nombre de acceso del usuario.
	public String getUsername() {
		return username;
	}

	// Metodo que sirve para establecer el nombre de acceso del usuario.
	public void setUsername(String username) {
		this.username = username;
	}

	// Metodo que sirve para obtener la contrasena cifrada del usuario.
	public String getPasswordHash() {
		return passwordHash;
	}

	// Metodo que sirve para establecer la contrasena cifrada del usuario.
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	// Metodo que sirve para comprobar si el usuario esta activo.
	public boolean isActivo() {
		return activo;
	}

	// Metodo que sirve para establecer el estado activo del usuario.
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	// Metodo que sirve para obtener el rol del usuario.
	public Rol getRol() {
		return rol;
	}

	// Metodo que sirve para establecer el rol del usuario.
	public void setRol(Rol rol) {
		this.rol = rol;
	}

	// Metodo que sirve para obtener el equipo entrenado por el usuario.
	public Equipo getEquipoEntrenado() {
		return equipoEntrenado;
	}

	// Metodo que sirve para establecer el equipo entrenado por el usuario.
	public void setEquipoEntrenado(Equipo equipoEntrenado) {
		this.equipoEntrenado = equipoEntrenado;
	}

	// Metodo que sirve para obtener los partidos arbitrados por el usuario.
	public List<Partido> getPartidosArbitrados() {
		return partidosArbitrados;
	}

	// Metodo que sirve para establecer los partidos arbitrados por el usuario.
	public void setPartidosArbitrados(List<Partido> partidosArbitrados) {
		this.partidosArbitrados = partidosArbitrados;
	}

	// Metodo que sirve para obtener las solicitudes creadas por el usuario.
	public List<SolicitudContacto> getSolicitudesCreadas() {
		return solicitudesCreadas;
	}

	// Metodo que sirve para establecer las solicitudes creadas por el usuario.
	public void setSolicitudesCreadas(List<SolicitudContacto> solicitudesCreadas) {
		this.solicitudesCreadas = solicitudesCreadas;
	}

}
