package com.basketAljarafe.controlador;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.basketAljarafe.dto.ActaPartidoDto;
import com.basketAljarafe.dto.FormularioCambioEntrenadorDto;
import com.basketAljarafe.dto.FormularioContactoDto;
import com.basketAljarafe.dto.FormularioEquipoDto;
import com.basketAljarafe.dto.FormularioJugadorDto;
import com.basketAljarafe.dto.FormularioUsuarioDto;
import com.basketAljarafe.servicio.ServicioArbitro;
import com.basketAljarafe.servicio.ServicioEntrenador;
import com.basketAljarafe.servicio.ServicioGerente;
import com.basketAljarafe.servicio.ServicioPublico;

@Controller
public class ControladorVista {

	private final ServicioPublico servicioPublico;
	private final ServicioGerente servicioGerente;
	private final ServicioEntrenador servicioEntrenador;
	private final ServicioArbitro servicioArbitro;

	// Metodo que sirve para crear el controlador de vistas con sus servicios de apoyo.
	public ControladorVista(ServicioPublico servicioPublico, ServicioGerente servicioGerente,
			ServicioEntrenador servicioEntrenador, ServicioArbitro servicioArbitro) {
		this.servicioPublico = servicioPublico;
		this.servicioGerente = servicioGerente;
		this.servicioEntrenador = servicioEntrenador;
		this.servicioArbitro = servicioArbitro;
	}

	@GetMapping("/")
	// Metodo que sirve para mostrar la pagina principal publica.
	public String mostrarInicio(Model model, Principal principal) {
		model.addAttribute("resumenInicio", servicioPublico.obtenerResumenInicio());
		model.addAttribute("usuarioActual", principal != null ? principal.getName() : null);
		return "inicio";
	}

	@GetMapping("/clasificacion")
	// Metodo que sirve para mostrar la pagina de clasificacion completa.
	public String mostrarClasificacion(Model model, Principal principal) {
		model.addAttribute("clasificacion", servicioPublico.obtenerClasificacionCompleta());
		model.addAttribute("usuarioActual", principal != null ? principal.getName() : null);
		return "clasificacion";
	}

	@GetMapping("/calendario")
	// Metodo que sirve para mostrar la pagina de calendario y resultados.
	public String mostrarCalendario(Model model, Principal principal) {
		model.addAttribute("partidos", servicioPublico.obtenerCalendarioCompleto());
		model.addAttribute("usuarioActual", principal != null ? principal.getName() : null);
		return "calendario";
	}

	@GetMapping("/estadisticas")
	// Metodo que sirve para mostrar la pagina de estadisticas individuales.
	public String mostrarEstadisticas(Model model, Principal principal) {
		model.addAttribute("estadisticas", servicioPublico.obtenerEstadisticasJugadores());
		model.addAttribute("usuarioActual", principal != null ? principal.getName() : null);
		return "estadisticas";
	}

	@GetMapping("/contacto")
	// Metodo que sirve para mostrar la pagina de contacto e inscripcion.
	public String mostrarContacto(Model model, Principal principal) {
		model.addAttribute("formularioContacto", new FormularioContactoDto());
		model.addAttribute("usuarioActual", principal != null ? principal.getName() : null);
		return "contacto";
	}

	@PostMapping("/contacto/enviar")
	// Metodo que sirve para registrar una solicitud de contacto desde la vista web.
	public String enviarContacto(@ModelAttribute FormularioContactoDto formularioContactoDto,
			RedirectAttributes redirectAttributes) {
		servicioPublico.registrarSolicitudContacto(formularioContactoDto);
		redirectAttributes.addFlashAttribute("mensajeExito", "Solicitud enviada correctamente.");
		return "redirect:/contacto";
	}

	@GetMapping("/login")
	// Metodo que sirve para mostrar la pagina de inicio de sesion.
	public String mostrarLogin() {
		return "login";
	}

	@GetMapping("/panel")
	// Metodo que sirve para redirigir al usuario autenticado al panel propio de su rol.
	public String redirigirPanel(Authentication authentication) {
		for (GrantedAuthority authority : authentication.getAuthorities()) {
			if ("ROLE_ADMIN".equals(authority.getAuthority()) || "ROLE_GERENTE".equals(authority.getAuthority())) {
				return "redirect:/gerente/administrar";
			}
			if ("ROLE_ARBITRO".equals(authority.getAuthority())) {
				return "redirect:/arbitro/ficha-partidos";
			}
			if ("ROLE_ENTRENADOR".equals(authority.getAuthority())) {
				return "redirect:/entrenador/administrar-equipo";
			}
		}

		return "redirect:/";
	}

	@GetMapping("/gerente/administrar")
	// Metodo que sirve para mostrar la pantalla de administracion del gerente.
	public String mostrarPanelGerente(Model model, Principal principal) {
		model.addAttribute("usuarios", servicioGerente.obtenerUsuarios());
		model.addAttribute("equipos", servicioGerente.obtenerEquipos());
		model.addAttribute("entrenadores", servicioGerente.obtenerEntrenadores());
		model.addAttribute("formularioUsuario", new FormularioUsuarioDto());
		model.addAttribute("formularioEquipo", new FormularioEquipoDto());
		model.addAttribute("formularioCambioEntrenador", new FormularioCambioEntrenadorDto());
		model.addAttribute("usuarioActual", principal != null ? principal.getName() : null);
		model.addAttribute("esAdmin", authenticationEsAdmin());
		return "gerente";
	}

	@PostMapping("/gerente/usuarios")
	// Metodo que sirve para crear un usuario desde la vista del gerente.
	public String crearUsuario(@ModelAttribute FormularioUsuarioDto formularioUsuarioDto,
			RedirectAttributes redirectAttributes) {
		servicioGerente.crearUsuario(formularioUsuarioDto, authenticationEsAdmin());
		redirectAttributes.addFlashAttribute("mensajeExito", "Usuario creado correctamente.");
		return "redirect:/gerente/administrar";
	}

	@PostMapping("/gerente/equipos")
	// Metodo que sirve para crear un equipo desde la vista del gerente.
	public String crearEquipo(@ModelAttribute FormularioEquipoDto formularioEquipoDto,
			RedirectAttributes redirectAttributes) {
		servicioGerente.crearEquipo(formularioEquipoDto);
		redirectAttributes.addFlashAttribute("mensajeExito", "Equipo creado correctamente.");
		return "redirect:/gerente/administrar";
	}

	@PostMapping("/gerente/equipos/cambiar-entrenador")
	// Metodo que sirve para cambiar el entrenador asociado a un equipo desde la vista del gerente.
	public String cambiarEntrenador(@ModelAttribute FormularioCambioEntrenadorDto formularioCambioEntrenadorDto,
			RedirectAttributes redirectAttributes) {
		servicioGerente.cambiarEntrenador(formularioCambioEntrenadorDto);
		redirectAttributes.addFlashAttribute("mensajeExito", "Entrenador actualizado correctamente.");
		return "redirect:/gerente/administrar";
	}

	@GetMapping("/entrenador/administrar-equipo")
	// Metodo que sirve para mostrar la pantalla del entrenador con su plantilla.
	public String mostrarPanelEntrenador(Model model, Principal principal) {
		model.addAttribute("equipo", servicioEntrenador.obtenerEquipoDelEntrenador(principal.getName()));
		model.addAttribute("jugadores", servicioEntrenador.obtenerJugadoresDelEntrenador(principal.getName()));
		model.addAttribute("formularioJugador", new FormularioJugadorDto());
		model.addAttribute("usuarioActual", principal != null ? principal.getName() : null);
		return "entrenador";
	}

	@PostMapping("/entrenador/jugadores")
	// Metodo que sirve para dar de alta un jugador desde la vista del entrenador.
	public String crearJugador(@ModelAttribute FormularioJugadorDto formularioJugadorDto, Principal principal,
			RedirectAttributes redirectAttributes) {
		servicioEntrenador.altaJugador(principal.getName(), formularioJugadorDto);
		redirectAttributes.addFlashAttribute("mensajeExito", "Jugador creado correctamente.");
		return "redirect:/entrenador/administrar-equipo";
	}

	@PostMapping("/entrenador/jugadores/{idJugador}/baja")
	// Metodo que sirve para dar de baja un jugador desde la vista del entrenador.
	public String darBajaJugador(@PathVariable Integer idJugador, Principal principal,
			RedirectAttributes redirectAttributes) {
		servicioEntrenador.bajaJugador(principal.getName(), idJugador);
		redirectAttributes.addFlashAttribute("mensajeExito", "Jugador dado de baja correctamente.");
		return "redirect:/entrenador/administrar-equipo";
	}

	@PostMapping("/entrenador/jugadores/{idJugador}/alta")
	// Metodo que sirve para dar de alta un jugador desde la vista del entrenador.
	public String darAltaJugador(@PathVariable Integer idJugador, Principal principal,
			RedirectAttributes redirectAttributes) {
		servicioEntrenador.altaJugadorExistente(principal.getName(), idJugador);
		redirectAttributes.addFlashAttribute("mensajeExito", "Jugador dado de alta correctamente.");
		return "redirect:/entrenador/administrar-equipo";
	}

	@GetMapping("/arbitro/ficha-partidos")
	// Metodo que sirve para mostrar la pantalla del arbitro con sus partidos.
	public String mostrarPanelArbitro(Model model, Principal principal) {
		model.addAttribute("partidos", servicioArbitro.obtenerPartidosDelArbitro(principal.getName()));
		model.addAttribute("usuarioActual", principal != null ? principal.getName() : null);
		return "arbitro";
	}

	@GetMapping("/arbitro/partidos/{idPartido}/acta")
	// Metodo que sirve para mostrar la ficha completa editable de un partido asociado al arbitro.
	public String mostrarActaArbitro(@PathVariable Integer idPartido, Principal principal, Model model) {
		model.addAttribute("actaPartido", servicioArbitro.obtenerActaPartido(principal.getName(), idPartido));
		model.addAttribute("usuarioActual", principal != null ? principal.getName() : null);
		return "arbitro-acta";
	}

	@PostMapping("/arbitro/partidos/{idPartido}/acta")
	// Metodo que sirve para registrar una ficha completa desde la vista del arbitro.
	public String registrarActaCompleta(@PathVariable Integer idPartido, @ModelAttribute ActaPartidoDto actaPartidoDto,
			Principal principal, RedirectAttributes redirectAttributes) {
		servicioArbitro.registrarActa(principal.getName(), idPartido, actaPartidoDto);
		redirectAttributes.addFlashAttribute("mensajeExito", "Acta registrada correctamente.");
		return "redirect:/arbitro/partidos/" + idPartido + "/acta";
	}

	// Metodo que sirve para comprobar si el usuario autenticado actual tiene rol de administrador.
	private boolean authenticationEsAdmin() {
		Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext()
				.getAuthentication();

		if (authentication == null) {
			return false;
		}

		for (GrantedAuthority authority : authentication.getAuthorities()) {
			if ("ROLE_ADMIN".equals(authority.getAuthority())) {
				return true;
			}
		}

		return false;
	}
}
