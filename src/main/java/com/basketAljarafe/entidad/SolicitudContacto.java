package com.basketAljarafe.entidad;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "SOLICITUD_CONTACTO")
@JsonIgnoreProperties({ "usuarioCreador" })
public class SolicitudContacto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_solicitud")
	private Integer idSolicitud;

	@Column(name = "nombre_contacto", length = 100)
	private String nombreContacto;

	@Column(name = "correo_contacto", length = 150)
	private String correoContacto;

	@Column(name = "telefono_contacto", length = 25)
	private String telefonoContacto;

	@Column(name = "mensaje", nullable = false, length = 500)
	private String mensaje;

	@Column(name = "fecha_creacion", nullable = false)
	private LocalDateTime fechaCreacion = LocalDateTime.now();

	// Conexion que sirve para relacionar la solicitud con un usuario interno cuando proceda.
	@ManyToOne
	@JoinColumn(name = "id_usuario_creador")
	private Usuario usuarioCreador;

	// Metodo que sirve para crear una solicitud de contacto vacia.
	public SolicitudContacto() {
	}

	// Metodo que sirve para obtener el identificador de la solicitud.
	public Integer getIdSolicitud() {
		return idSolicitud;
	}

	// Metodo que sirve para establecer el identificador de la solicitud.
	public void setIdSolicitud(Integer idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	// Metodo que sirve para obtener el nombre del contacto.
	public String getNombreContacto() {
		return nombreContacto;
	}

	// Metodo que sirve para establecer el nombre del contacto.
	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}

	// Metodo que sirve para obtener el correo del contacto.
	public String getCorreoContacto() {
		return correoContacto;
	}

	// Metodo que sirve para establecer el correo del contacto.
	public void setCorreoContacto(String correoContacto) {
		this.correoContacto = correoContacto;
	}

	// Metodo que sirve para obtener el telefono del contacto.
	public String getTelefonoContacto() {
		return telefonoContacto;
	}

	// Metodo que sirve para establecer el telefono del contacto.
	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
	}

	// Metodo que sirve para obtener el mensaje del contacto.
	public String getMensaje() {
		return mensaje;
	}

	// Metodo que sirve para establecer el mensaje del contacto.
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	// Metodo que sirve para obtener la fecha de creacion de la solicitud.
	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	// Metodo que sirve para establecer la fecha de creacion de la solicitud.
	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	// Metodo que sirve para obtener el usuario creador de la solicitud.
	public Usuario getUsuarioCreador() {
		return usuarioCreador;
	}

	// Metodo que sirve para establecer el usuario creador de la solicitud.
	public void setUsuarioCreador(Usuario usuarioCreador) {
		this.usuarioCreador = usuarioCreador;
	}
}
