package com.basketAljarafe.controlador;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.basketAljarafe.dto.ClasificacionEquipoDto;
import com.basketAljarafe.dto.ActaPartidoDto;
import com.basketAljarafe.dto.EstadisticaJugadorDto;
import com.basketAljarafe.dto.FormularioContactoDto;
import com.basketAljarafe.dto.ResumenInicioDto;
import com.basketAljarafe.entidad.Partido;
import com.basketAljarafe.entidad.SolicitudContacto;
import com.basketAljarafe.servicio.ServicioPublico;

@RestController
@RequestMapping("/api/publico")
public class ControladorRestPublico {

	private final ServicioPublico servicioPublico;

	// Metodo que sirve para crear el controlador REST publico.
	public ControladorRestPublico(ServicioPublico servicioPublico) {
		this.servicioPublico = servicioPublico;
	}

	@GetMapping("/inicio")
	// Metodo que sirve para devolver la informacion publica de la pagina principal.
	public ResumenInicioDto obtenerInicio() {
		return servicioPublico.obtenerResumenInicio();
	}

	@GetMapping("/clasificacion")
	// Metodo que sirve para devolver la clasificacion completa en formato JSON.
	public List<ClasificacionEquipoDto> obtenerClasificacion() {
		return servicioPublico.obtenerClasificacionCompleta();
	}

	@GetMapping("/calendario")
	// Metodo que sirve para devolver el calendario completo en formato JSON.
	public List<Partido> obtenerCalendario() {
		return servicioPublico.obtenerCalendarioCompleto();
	}

	@GetMapping("/partidos/{idPartido}")
	// Metodo que sirve para devolver la ficha publica de un partido en formato JSON.
	public ActaPartidoDto obtenerFichaPartido(@PathVariable Integer idPartido) {
		return servicioPublico.obtenerFichaPartido(idPartido);
	}

	@GetMapping("/estadisticas")
	// Metodo que sirve para devolver las estadisticas agregadas de jugadores en formato JSON.
	public List<EstadisticaJugadorDto> obtenerEstadisticas() {
		return servicioPublico.obtenerEstadisticasJugadores();
	}

	@PostMapping("/contacto")
	@ResponseStatus(HttpStatus.CREATED)
	// Metodo que sirve para registrar una solicitud de contacto desde la API.
	public SolicitudContacto registrarContacto(@RequestBody FormularioContactoDto formularioContactoDto) {
		return servicioPublico.registrarSolicitudContacto(formularioContactoDto);
	}
}
