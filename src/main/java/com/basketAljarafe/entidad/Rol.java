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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ROL")
@JsonIgnoreProperties({ "usuarios" })
public class Rol {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_rol")
	private Integer idRol;

	@Column(name = "nombre_rol", nullable = false, unique = true, length = 20)
	private String nombreRol;

	// Conexion que sirve para relacionar un rol con los usuarios que lo poseen.
	@OneToMany(mappedBy = "rol", cascade = CascadeType.ALL)
	private List<Usuario> usuarios = new ArrayList<>();

	// Metodo que sirve para crear un rol vacio.
	public Rol() {
	}

	// Metodo que sirve para crear un rol con su nombre.
	public Rol(String nombreRol) {
		this.nombreRol = nombreRol;
	}

	// Metodo que sirve para obtener el identificador del rol.
	public Integer getIdRol() {
		return idRol;
	}

	// Metodo que sirve para establecer el identificador del rol.
	public void setIdRol(Integer idRol) {
		this.idRol = idRol;
	}

	// Metodo que sirve para obtener el nombre del rol.
	public String getNombreRol() {
		return nombreRol;
	}

	// Metodo que sirve para establecer el nombre del rol.
	public void setNombreRol(String nombreRol) {
		this.nombreRol = nombreRol;
	}

	// Metodo que sirve para obtener los usuarios asociados al rol.
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	// Metodo que sirve para establecer los usuarios asociados al rol.
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
}
