package com.basketAljarafe.servicio;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.basketAljarafe.dto.FormularioCambioEntrenadorDto;
import com.basketAljarafe.dto.FormularioEquipoDto;
import com.basketAljarafe.dto.FormularioUsuarioDto;
import com.basketAljarafe.entidad.Equipo;
import com.basketAljarafe.entidad.Rol;
import com.basketAljarafe.entidad.Usuario;
import com.basketAljarafe.repositorio.EquipoRepositorio;
import com.basketAljarafe.repositorio.RolRepositorio;
import com.basketAljarafe.repositorio.UsuarioRepositorio;

@Service
public class ServicioGerente {

	private final UsuarioRepositorio usuarioRepositorio;
	private final RolRepositorio rolRepositorio;
	private final EquipoRepositorio equipoRepositorio;
	private final PasswordEncoder passwordEncoder;

	// Metodo que sirve para crear el servicio de gerente con sus dependencias.
	public ServicioGerente(UsuarioRepositorio usuarioRepositorio, RolRepositorio rolRepositorio,
			EquipoRepositorio equipoRepositorio, PasswordEncoder passwordEncoder) {
		this.usuarioRepositorio = usuarioRepositorio;
		this.rolRepositorio = rolRepositorio;
		this.equipoRepositorio = equipoRepositorio;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(readOnly = true)
	// Metodo que sirve para obtener todos los usuarios registrados.
	public List<Usuario> obtenerUsuarios() {
		return usuarioRepositorio.findAll();
	}

	@Transactional(readOnly = true)
	// Metodo que sirve para obtener todos los equipos registrados.
	public List<Equipo> obtenerEquipos() {
		return equipoRepositorio.findAll();
	}

	@Transactional(readOnly = true)
	// Metodo que sirve para obtener los entrenadores disponibles para asignar a un equipo.
	public List<Usuario> obtenerEntrenadores() {
		return usuarioRepositorio.findByRolNombreRolOrderByNombreUsuarioAsc("ENTRENADOR").stream()
				.filter(Usuario::isActivo)
				.filter(usuario -> usuario.getEquipoEntrenado() == null)
				.toList();
	}

	@Transactional
	// Metodo que sirve para crear un usuario nuevo con el rol indicado.
	public Usuario crearUsuario(FormularioUsuarioDto formularioUsuarioDto, boolean esAdmin) {
		Rol rol = rolRepositorio.findByNombreRol(formularioUsuarioDto.getNombreRol())
				.orElseThrow(() -> new IllegalArgumentException("No existe el rol indicado"));

		if (!esAdmin && !"ARBITRO".equals(rol.getNombreRol()) && !"ENTRENADOR".equals(rol.getNombreRol())) {
			throw new IllegalArgumentException("El gerente solo puede crear arbitros y entrenadores");
		}

		Usuario usuario = new Usuario();
		usuario.setNombreUsuario(formularioUsuarioDto.getNombreUsuario());
		usuario.setUsername(formularioUsuarioDto.getUsername());
		usuario.setPasswordHash(passwordEncoder.encode(formularioUsuarioDto.getPassword()));
		usuario.setRol(rol);
		usuario.setActivo(true);
		return usuarioRepositorio.save(usuario);
	}

	@Transactional
	// Metodo que sirve para crear un equipo nuevo con su entrenador.
	public Equipo crearEquipo(FormularioEquipoDto formularioEquipoDto) {
		Usuario entrenador = usuarioRepositorio.findById(formularioEquipoDto.getIdEntrenador())
				.orElseThrow(() -> new IllegalArgumentException("No existe el entrenador indicado"));

		Equipo equipo = new Equipo();
		equipo.setNombre(formularioEquipoDto.getNombre());
		equipo.setEntrenador(entrenador);
		return equipoRepositorio.save(equipo);
	}

	@Transactional
	// Metodo que sirve para cambiar el entrenador asociado a un equipo existente.
	public Equipo cambiarEntrenador(FormularioCambioEntrenadorDto formularioCambioEntrenadorDto) {
		Equipo equipo = equipoRepositorio.findById(formularioCambioEntrenadorDto.getIdEquipo())
				.orElseThrow(() -> new IllegalArgumentException("No existe el equipo indicado"));
		Usuario entrenador = usuarioRepositorio.findById(formularioCambioEntrenadorDto.getIdEntrenador())
				.orElseThrow(() -> new IllegalArgumentException("No existe el entrenador indicado"));

		equipo.setEntrenador(entrenador);
		return equipoRepositorio.save(equipo);
	}

	@Transactional
	// Metodo que sirve para desactivar un usuario desde el panel de administracion.
	public Usuario desactivarUsuario(Integer idUsuario, String usernameActual, boolean esAdmin) {
		Usuario usuario = obtenerUsuarioParaCambioEstado(idUsuario, esAdmin);

		if (usuario.getUsername().equals(usernameActual)) {
			throw new IllegalArgumentException("No es posible desactivar la cuenta del usuario autenticado");
		}

		usuario.setActivo(false);
		return usuarioRepositorio.save(usuario);
	}

	@Transactional
	// Metodo que sirve para reactivar un usuario desde el panel de administracion.
	public Usuario activarUsuario(Integer idUsuario, boolean esAdmin) {
		Usuario usuario = obtenerUsuarioParaCambioEstado(idUsuario, esAdmin);
		usuario.setActivo(true);
		return usuarioRepositorio.save(usuario);
	}

	// Metodo que sirve para obtener un usuario y validar si el perfil actual puede cambiar su estado.
	private Usuario obtenerUsuarioParaCambioEstado(Integer idUsuario, boolean esAdmin) {
		Usuario usuario = usuarioRepositorio.findById(idUsuario)
				.orElseThrow(() -> new IllegalArgumentException("No existe el usuario indicado"));

		if (esAdmin || "ARBITRO".equals(usuario.getRol().getNombreRol())
				|| "ENTRENADOR".equals(usuario.getRol().getNombreRol())) {
			return usuario;
		}

		throw new IllegalArgumentException("El gerente solo puede activar o desactivar arbitros y entrenadores");
	}
}
