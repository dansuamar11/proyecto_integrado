package com.basketAljarafe.controlador;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.basketAljarafe.dto.ActaPartidoDto;
import com.basketAljarafe.dto.FormularioCambioEntrenadorDto;
import com.basketAljarafe.dto.FormularioEquipoDto;
import com.basketAljarafe.dto.FormularioJugadorDto;
import com.basketAljarafe.dto.FormularioLoginDto;
import com.basketAljarafe.dto.FormularioUsuarioDto;
import com.basketAljarafe.entidad.Equipo;
import com.basketAljarafe.entidad.Jugador;
import com.basketAljarafe.entidad.Partido;
import com.basketAljarafe.entidad.Usuario;
import com.basketAljarafe.servicio.ServicioArbitro;
import com.basketAljarafe.servicio.ServicioEntrenador;
import com.basketAljarafe.servicio.ServicioGerente;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class ControladorRestPrivado {

	private final ServicioGerente servicioGerente;
	private final ServicioEntrenador servicioEntrenador;
	private final ServicioArbitro servicioArbitro;

	// Metodo que sirve para crear el controlador REST privado.
	public ControladorRestPrivado(ServicioGerente servicioGerente, ServicioEntrenador servicioEntrenador,
			ServicioArbitro servicioArbitro) {
		this.servicioGerente = servicioGerente;
		this.servicioEntrenador = servicioEntrenador;
		this.servicioArbitro = servicioArbitro;
	}

	@GetMapping("/auth/me")
	// Metodo que sirve para devolver el usuario autenticado actual.
	public Map<String, Object> obtenerUsuarioActual(Principal principal, Authentication authentication) {
		return construirRespuestaSesion(authentication, principal);
	}

	@PostMapping(value = "/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	// Metodo que sirve para iniciar sesion desde la API REST y devolver el estado autenticado.
	public Map<String, Object> iniciarSesion(@RequestBody FormularioLoginDto formularioLoginDto,
			HttpServletRequest httpServletRequest) throws ServletException {
		httpServletRequest.login(formularioLoginDto.getUsername(), formularioLoginDto.getPassword());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return construirRespuestaSesion(authentication, httpServletRequest.getUserPrincipal());
	}

	@PostMapping("/auth/logout")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	// Metodo que sirve para cerrar la sesion autenticada actual desde la API REST.
	public void cerrarSesion(HttpServletRequest httpServletRequest) throws ServletException {
		httpServletRequest.logout();
	}

	@GetMapping("/auth/ping")
	// Metodo que sirve para comprobar que la API privada esta disponible.
	public Map<String, Object> comprobarDisponibilidad() {
		return Map.of("disponible", true);
	}

	@GetMapping("/gerente/usuarios")
	// Metodo que sirve para devolver todos los usuarios para el gerente.
	public List<Usuario> obtenerUsuarios() {
		return servicioGerente.obtenerUsuarios();
	}

	@PostMapping("/gerente/usuarios")
	@ResponseStatus(HttpStatus.CREATED)
	// Metodo que sirve para crear un usuario desde la API del gerente.
	public Usuario crearUsuario(@RequestBody FormularioUsuarioDto formularioUsuarioDto, Authentication authentication) {
		return servicioGerente.crearUsuario(formularioUsuarioDto, esAdmin(authentication));
	}

	@PostMapping("/gerente/usuarios/{idUsuario}/desactivar")
	// Metodo que sirve para desactivar un usuario desde la API del gerente.
	public Usuario desactivarUsuario(@PathVariable Integer idUsuario, Authentication authentication) {
		return servicioGerente.desactivarUsuario(idUsuario, authentication.getName(), esAdmin(authentication));
	}

	@PostMapping("/gerente/usuarios/{idUsuario}/activar")
	// Metodo que sirve para reactivar un usuario desde la API del gerente.
	public Usuario activarUsuario(@PathVariable Integer idUsuario, Authentication authentication) {
		return servicioGerente.activarUsuario(idUsuario, esAdmin(authentication));
	}

	@GetMapping("/gerente/equipos")
	// Metodo que sirve para devolver todos los equipos para el gerente.
	public List<Equipo> obtenerEquipos() {
		return servicioGerente.obtenerEquipos();
	}

	@GetMapping("/gerente/entrenadores")
	// Metodo que sirve para devolver todos los entrenadores disponibles para el gerente.
	public List<Usuario> obtenerEntrenadores() {
		return servicioGerente.obtenerEntrenadores();
	}

	@PostMapping("/gerente/equipos")
	@ResponseStatus(HttpStatus.CREATED)
	// Metodo que sirve para crear un equipo desde la API del gerente.
	public Equipo crearEquipo(@RequestBody FormularioEquipoDto formularioEquipoDto) {
		return servicioGerente.crearEquipo(formularioEquipoDto);
	}

	@PostMapping("/gerente/equipos/cambiar-entrenador")
	// Metodo que sirve para cambiar el entrenador de un equipo desde la API del gerente.
	public Equipo cambiarEntrenador(@RequestBody FormularioCambioEntrenadorDto formularioCambioEntrenadorDto) {
		return servicioGerente.cambiarEntrenador(formularioCambioEntrenadorDto);
	}

	@GetMapping("/entrenador/equipo")
	// Metodo que sirve para devolver el equipo del entrenador autenticado.
	public Equipo obtenerEquipo(Principal principal) {
		return servicioEntrenador.obtenerEquipoDelEntrenador(principal.getName());
	}

	@GetMapping("/entrenador/jugadores")
	// Metodo que sirve para devolver los jugadores del entrenador autenticado.
	public List<Jugador> obtenerJugadores(Principal principal) {
		return servicioEntrenador.obtenerJugadoresDelEntrenador(principal.getName());
	}

	@PostMapping("/entrenador/jugadores")
	@ResponseStatus(HttpStatus.CREATED)
	// Metodo que sirve para crear un jugador desde la API del entrenador.
	public Jugador altaJugador(@RequestBody FormularioJugadorDto formularioJugadorDto, Principal principal) {
		return servicioEntrenador.altaJugador(principal.getName(), formularioJugadorDto);
	}

	@PostMapping("/entrenador/jugadores/{idJugador}/baja")
	// Metodo que sirve para dar de baja a un jugador desde la API del entrenador.
	public Jugador bajaJugador(@PathVariable Integer idJugador, Principal principal) {
		return servicioEntrenador.bajaJugador(principal.getName(), idJugador);
	}

	@PostMapping("/entrenador/jugadores/{idJugador}/alta")
	// Metodo que sirve para dar de alta a un jugador desde la API del entrenador.
	public Jugador altaJugadorExistente(@PathVariable Integer idJugador, Principal principal) {
		return servicioEntrenador.altaJugadorExistente(principal.getName(), idJugador);
	}

	@GetMapping("/arbitro/partidos")
	// Metodo que sirve para devolver los partidos del arbitro autenticado.
	public List<Partido> obtenerPartidosArbitro(Principal principal) {
		return servicioArbitro.obtenerPartidosDelArbitro(principal.getName());
	}

	@GetMapping("/arbitro/partidos/{idPartido}/acta")
	// Metodo que sirve para devolver la ficha completa de un partido del arbitro autenticado.
	public ActaPartidoDto obtenerActa(@PathVariable Integer idPartido, Principal principal) {
		return servicioArbitro.obtenerActaPartido(principal.getName(), idPartido);
	}

	@PostMapping("/arbitro/partidos/{idPartido}/acta")
	// Metodo que sirve para registrar un acta desde la API del arbitro.
	public Partido registrarActa(@PathVariable Integer idPartido, @RequestBody ActaPartidoDto actaPartidoDto,
			Principal principal) {
		return servicioArbitro.registrarActa(principal.getName(), idPartido, actaPartidoDto);
	}

	// Metodo que sirve para comprobar si el usuario autenticado actual tiene rol de administrador.
	private boolean esAdmin(Authentication authentication) {
		return authentication.getAuthorities().stream()
				.anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
	}

	// Metodo que sirve para construir la respuesta JSON de sesion para el frontend.
	private Map<String, Object> construirRespuestaSesion(Authentication authentication, Principal principal) {
		if (authentication == null || principal == null || !authentication.isAuthenticated()
				|| "anonymousUser".equals(principal.getName())) {
			return Map.of(
					"authenticated", false,
					"username", "",
					"roles", List.of());
		}

		return Map.of(
				"authenticated", true,
				"username", principal.getName(),
				"roles", authentication.getAuthorities().stream()
						.map(grantedAuthority -> grantedAuthority.getAuthority())
						.collect(Collectors.toList()));
	}
}
