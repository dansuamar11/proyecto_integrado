package com.basketAljarafe.servicio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.basketAljarafe.dto.FormularioAsignacionArbitroDto;
import com.basketAljarafe.dto.FormularioCambioEntrenadorDto;
import com.basketAljarafe.dto.FormularioEquipoDto;
import com.basketAljarafe.dto.FormularioGenerarPartidoDto;
import com.basketAljarafe.dto.FormularioUsuarioDto;
import com.basketAljarafe.entidad.Equipo;
import com.basketAljarafe.entidad.EstadoPartido;
import com.basketAljarafe.entidad.Partido;
import com.basketAljarafe.entidad.Rol;
import com.basketAljarafe.entidad.SolicitudContacto;
import com.basketAljarafe.entidad.Usuario;
import com.basketAljarafe.repositorio.EquipoRepositorio;
import com.basketAljarafe.repositorio.PartidoRepositorio;
import com.basketAljarafe.repositorio.RolRepositorio;
import com.basketAljarafe.repositorio.SolicitudContactoRepositorio;
import com.basketAljarafe.repositorio.UsuarioRepositorio;

@Service
public class ServicioGerente {

	private final UsuarioRepositorio usuarioRepositorio;
	private final RolRepositorio rolRepositorio;
	private final EquipoRepositorio equipoRepositorio;
	private final PartidoRepositorio partidoRepositorio;
	private final SolicitudContactoRepositorio solicitudContactoRepositorio;
	private final PasswordEncoder passwordEncoder;

	// Metodo que sirve para crear el servicio de gerente con sus dependencias.
	public ServicioGerente(UsuarioRepositorio usuarioRepositorio, RolRepositorio rolRepositorio,
			EquipoRepositorio equipoRepositorio, PartidoRepositorio partidoRepositorio,
			SolicitudContactoRepositorio solicitudContactoRepositorio,
			PasswordEncoder passwordEncoder) {
		this.usuarioRepositorio = usuarioRepositorio;
		this.rolRepositorio = rolRepositorio;
		this.equipoRepositorio = equipoRepositorio;
		this.partidoRepositorio = partidoRepositorio;
		this.solicitudContactoRepositorio = solicitudContactoRepositorio;
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
		
		try {
        return usuarioRepositorio.save(usuario);
    	} catch (org.springframework.dao.DataIntegrityViolationException e) {
        throw new org.springframework.web.server.ResponseStatusException(
            org.springframework.http.HttpStatus.CONFLICT,
            "El nombre de usuario ya está en uso."
        );
		}
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

	// Metodo que sirve para obtener los arbitros activos disponibles para asignar partidos.
	public List<Usuario> obtenerArbitros() {
		return usuarioRepositorio.findByRolNombreRolOrderByNombreUsuarioAsc("ARBITRO").stream()
				.filter(Usuario::isActivo)
				.toList();
	}

	// Metodo que sirve para obtener los partidos pendientes que no tienen arbitro asignado.
	@Transactional(readOnly = true)
	public List<Partido> obtenerPartidosSinArbitro() {
		return partidoRepositorio.findByEstadoAndArbitroIsNullOrderByFechaAsc(EstadoPartido.PENDIENTE);
	}

	// Metodo que sirve para obtener todos los partidos pendientes para permitir asignar o cambiar arbitro.
	@Transactional(readOnly = true)
	public List<Partido> obtenerPartidosPendientes() {
		return partidoRepositorio.findByEstadoOrderByFechaAsc(EstadoPartido.PENDIENTE);
	}

	// Metodo que sirve para obtener las solicitudes recibidas desde la pagina de contacto.
	public List<SolicitudContacto> obtenerSolicitudesContacto() {
		return solicitudContactoRepositorio.findAllByOrderByFechaCreacionDescIdSolicitudDesc();
	}

	// Metodo que sirve para eliminar una solicitud de contacto del panel de gestion.
	public void eliminarSolicitudContacto(Integer idSolicitud) {
		SolicitudContacto solicitudContacto = solicitudContactoRepositorio.findById(idSolicitud)
				.orElseThrow(() -> new IllegalArgumentException("No existe la solicitud indicada"));
		solicitudContactoRepositorio.delete(solicitudContacto);
	}

	@Transactional
	// Metodo que sirve para asignar o cambiar un arbitro activo en un partido pendiente.
	public Partido asignarArbitroAPartido(FormularioAsignacionArbitroDto formulario) {
		Partido partido = partidoRepositorio.findById(formulario.getIdPartido())
				.orElseThrow(() -> new IllegalArgumentException("No existe el partido indicado"));

		if (partido.getEstado() != EstadoPartido.PENDIENTE) {
			throw new IllegalArgumentException("Solo se puede asignar arbitro a partidos pendientes");
		}

		Usuario arbitro = usuarioRepositorio.findById(formulario.getIdArbitro())
				.orElseThrow(() -> new IllegalArgumentException("No existe el arbitro indicado"));

		if (!arbitro.isActivo() || !"ARBITRO".equals(arbitro.getRol().getNombreRol())) {
			throw new IllegalArgumentException("El usuario seleccionado no es un arbitro activo");
		}

		partido.setArbitro(arbitro);
		return partidoRepositorio.save(partido);
	}

	@Transactional
	// Metodo que sirve para crear un nuevo partido con los datos indicados.
	public Partido generarPartido(FormularioGenerarPartidoDto formulario) {
		Equipo equipoLocal = equipoRepositorio.findById(formulario.getIdEquipoLocal())
				.orElseThrow(() -> new IllegalArgumentException("No existe el equipo local indicado"));

		Equipo equipoVisitante = equipoRepositorio.findById(formulario.getIdEquipoVisitante())
				.orElseThrow(() -> new IllegalArgumentException("No existe el equipo visitante indicado"));

		if (equipoLocal.getIdEquipo().equals(equipoVisitante.getIdEquipo())) {
			throw new IllegalArgumentException("El equipo local y el equipo visitante deben ser diferentes");
		}

		Usuario arbitro = null;
		if (formulario.getIdArbitro() != null) {
			arbitro = usuarioRepositorio.findById(formulario.getIdArbitro())
					.orElseThrow(() -> new IllegalArgumentException("No existe el arbitro indicado"));

			if (!arbitro.isActivo() || !"ARBITRO".equals(arbitro.getRol().getNombreRol())) {
				throw new IllegalArgumentException("El usuario seleccionado no es un arbitro activo");
			}
		}

		if (formulario.getFecha() == null) {
			throw new IllegalArgumentException("La fecha del partido es obligatoria");
		}

		validarHorarioPartido(formulario.getFecha());

		if (partidoRepositorio.existsByFecha(formulario.getFecha())) {
			throw new IllegalArgumentException("Ya existe un partido programado en esa fecha y hora");
		}

		validarEquiposSinPartidoMismoDia(formulario.getFecha(), equipoLocal.getIdEquipo(), equipoVisitante.getIdEquipo());

		Partido partido = new Partido(equipoLocal, equipoVisitante, formulario.getFecha(), arbitro);
		partido.setEstado(EstadoPartido.PENDIENTE);
		return partidoRepositorio.save(partido);
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

	// Metodo que sirve para limitar la creacion de partidos a las franjas horarias permitidas.
	private void validarHorarioPartido(LocalDateTime fecha) {
		Set<Integer> horasPermitidas = Set.of(12, 13, 16, 17, 18);

		if (!horasPermitidas.contains(fecha.getHour()) || fecha.getMinute() != 0 || fecha.getSecond() != 0
				|| fecha.getNano() != 0) {
			throw new IllegalArgumentException(
					"Solo se permiten partidos a las 12:00, 13:00, 16:00, 17:00 o 18:00");
		}
	}

	// Metodo que sirve para impedir que un equipo tenga dos partidos el mismo dia.
	private void validarEquiposSinPartidoMismoDia(LocalDateTime fecha, Integer idEquipoLocal, Integer idEquipoVisitante) {
		LocalDateTime inicioDia = fecha.toLocalDate().atStartOfDay();
		LocalDateTime finDia = inicioDia.plusDays(1);

		if (partidoRepositorio.countPartidosMismoDiaParaEquipos(inicioDia, finDia,
				List.of(idEquipoLocal, idEquipoVisitante)) > 0) {
			throw new IllegalArgumentException("Alguno de los equipos seleccionados ya tiene un partido ese mismo dia");
		}
	}
}
