package com.basketAljarafe.servicio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.basketAljarafe.dto.ClasificacionEquipoDto;
import com.basketAljarafe.dto.ActaPartidoDto;
import com.basketAljarafe.dto.EstadisticaJugadorDto;
import com.basketAljarafe.dto.EstadisticaJugadorPartidoDto;
import com.basketAljarafe.dto.FormularioContactoDto;
import com.basketAljarafe.dto.ResumenInicioDto;
import com.basketAljarafe.entidad.Equipo;
import com.basketAljarafe.entidad.EstadisticaPartido;
import com.basketAljarafe.entidad.EstadoPartido;
import com.basketAljarafe.entidad.Jugador;
import com.basketAljarafe.entidad.Partido;
import com.basketAljarafe.entidad.SolicitudContacto;
import com.basketAljarafe.repositorio.EquipoRepositorio;
import com.basketAljarafe.repositorio.EstadisticaPartidoRepositorio;
import com.basketAljarafe.repositorio.PartidoRepositorio;
import com.basketAljarafe.repositorio.SolicitudContactoRepositorio;

@Service
public class ServicioPublico {

	private final EquipoRepositorio equipoRepositorio;
	private final PartidoRepositorio partidoRepositorio;
	private final EstadisticaPartidoRepositorio estadisticaPartidoRepositorio;
	private final SolicitudContactoRepositorio solicitudContactoRepositorio;

	// Metodo que sirve para crear el servicio publico con sus repositorios de apoyo.
	public ServicioPublico(EquipoRepositorio equipoRepositorio, PartidoRepositorio partidoRepositorio,
			EstadisticaPartidoRepositorio estadisticaPartidoRepositorio,
			SolicitudContactoRepositorio solicitudContactoRepositorio) {
		this.equipoRepositorio = equipoRepositorio;
		this.partidoRepositorio = partidoRepositorio;
		this.estadisticaPartidoRepositorio = estadisticaPartidoRepositorio;
		this.solicitudContactoRepositorio = solicitudContactoRepositorio;
	}

	@Transactional(readOnly = true)
	// Metodo que sirve para obtener la informacion de inicio de la aplicacion.
	public ResumenInicioDto obtenerResumenInicio() {
		List<ClasificacionEquipoDto> clasificacion = obtenerClasificacionCompleta();
		List<ClasificacionEquipoDto> clasificacionReducida = clasificacion.stream().limit(4).toList();
		List<Partido> ultimosPartidosJugados = partidoRepositorio.findByEstadoOrderByFechaAsc(EstadoPartido.JUGADO).stream()
				.sorted(Comparator.comparing(Partido::getFecha).reversed())
				.limit(2)
				.toList();

		return new ResumenInicioDto("Liga Local del Aljarafe",
				"Competición local de baloncesto con gestión de equipos, arbitros, entrenadores y estadísticas.",
				clasificacionReducida,
				ultimosPartidosJugados);
	}

	@Transactional(readOnly = true)
	// Metodo que sirve para calcular la clasificacion completa de la liga.
	public List<ClasificacionEquipoDto> obtenerClasificacionCompleta() {
		List<Equipo> equipos = equipoRepositorio.findAll();
		List<Partido> partidosJugados = partidoRepositorio.findByEstadoOrderByFechaAsc(EstadoPartido.JUGADO);
		Map<Integer, ResumenEquipo> resumenes = new HashMap<>();

		for (Equipo equipo : equipos) {
			resumenes.put(equipo.getIdEquipo(), new ResumenEquipo(equipo));
		}

		for (Partido partido : partidosJugados) {
			ResumenEquipo resumenLocal = resumenes.get(partido.getEquipoLocal().getIdEquipo());
			ResumenEquipo resumenVisitante = resumenes.get(partido.getEquipoVisitante().getIdEquipo());

			resumenLocal.partidosJugados++;
			resumenVisitante.partidosJugados++;
			resumenLocal.puntosFavor += partido.getPuntosLocal();
			resumenLocal.puntosContra += partido.getPuntosVisitante();
			resumenVisitante.puntosFavor += partido.getPuntosVisitante();
			resumenVisitante.puntosContra += partido.getPuntosLocal();

			if (partido.getPuntosLocal() > partido.getPuntosVisitante()) {
				resumenLocal.partidosGanados++;
			} else if (partido.getPuntosVisitante() > partido.getPuntosLocal()) {
				resumenVisitante.partidosGanados++;
			}
		}

		for (EstadisticaPartido estadistica : estadisticaPartidoRepositorio.findAll()) {
			ResumenEquipo resumen = resumenes.get(estadistica.getJugador().getEquipo().getIdEquipo());
			if (resumen != null) {
				resumen.rebotes += estadistica.getRebotes();
				resumen.asistencias += estadistica.getAsistencias();
			}
		}

		List<ResumenEquipo> clasificacionOrdenada = new ArrayList<>(resumenes.values());
		clasificacionOrdenada.sort(Comparator
				.comparingInt(ResumenEquipo::getPartidosGanados).reversed()
				.thenComparingInt(ResumenEquipo::getDiferenciaPuntos).reversed()
				.thenComparingInt(ResumenEquipo::getPuntosFavor).reversed()
				.thenComparing(resumen -> resumen.equipo.getNombre()));

		List<ClasificacionEquipoDto> resultado = new ArrayList<>();
		int posicion = 1;
		for (ResumenEquipo resumen : clasificacionOrdenada) {
			resultado.add(new ClasificacionEquipoDto(posicion, resumen.equipo.getIdEquipo(), resumen.equipo.getNombre(),
					resumen.partidosJugados, resumen.partidosGanados, resumen.puntosFavor, resumen.puntosContra,
					resumen.getDiferenciaPuntos(), resumen.rebotes, resumen.asistencias));
			posicion++;
		}

		return resultado;
	}

	@Transactional(readOnly = true)
	// Metodo que sirve para obtener el calendario completo ordenado por fecha.
	public List<Partido> obtenerCalendarioCompleto() {
		return partidoRepositorio.findAllByOrderByFechaAsc();
	}

	@Transactional(readOnly = true)
	// Metodo que sirve para obtener la ficha publica completa de un partido.
	public ActaPartidoDto obtenerFichaPartido(Integer idPartido) {
		Partido partido = partidoRepositorio.findById(idPartido)
				.orElseThrow(() -> new IllegalArgumentException("No existe el partido indicado"));
		ActaPartidoDto actaPartidoDto = new ActaPartidoDto();
		actaPartidoDto.setIdPartido(partido.getIdPartido());
		actaPartidoDto.setNombreEquipoLocal(partido.getEquipoLocal().getNombre());
		actaPartidoDto.setNombreEquipoVisitante(partido.getEquipoVisitante().getNombre());
		actaPartidoDto.setPuntosLocal(partido.getPuntosLocal());
		actaPartidoDto.setPuntosVisitante(partido.getPuntosVisitante());
		actaPartidoDto.setEstadisticasJugadores(
				partido.getEquipoLocal().getJugadores().stream()
						.map(jugador -> construirEstadisticaJugador(partido, jugador))
						.collect(Collectors.toCollection(ArrayList::new)));
		actaPartidoDto.getEstadisticasJugadores().addAll(
				partido.getEquipoVisitante().getJugadores().stream()
						.map(jugador -> construirEstadisticaJugador(partido, jugador))
						.toList());
		return actaPartidoDto;
	}

	@Transactional(readOnly = true)
	// Metodo que sirve para obtener las estadisticas agregadas de los jugadores.
	public List<EstadisticaJugadorDto> obtenerEstadisticasJugadores() {
		Map<Integer, EstadisticaJugadorDto> acumuladas = new HashMap<>();

		for (EstadisticaPartido estadistica : estadisticaPartidoRepositorio.findAll()) {
			Integer idJugador = estadistica.getJugador().getIdJugador();
			EstadisticaJugadorDto previa = acumuladas.get(idJugador);

			if (previa == null) {
				acumuladas.put(idJugador, new EstadisticaJugadorDto(idJugador,
						estadistica.getJugador().getNombre() + " " + estadistica.getJugador().getApellidos(),
						estadistica.getJugador().getDorsal(),
						estadistica.getJugador().getEquipo().getNombre(), estadistica.getPuntos(), estadistica.getRebotes(),
						estadistica.getAsistencias(), estadistica.getMinutos(), estadistica.getFaltas()));
			} else {
				acumuladas.put(idJugador, new EstadisticaJugadorDto(idJugador, previa.nombreJugador(),
						previa.dorsal(),
						previa.nombreEquipo(), previa.puntos() + estadistica.getPuntos(),
						previa.rebotes() + estadistica.getRebotes(), previa.asistencias() + estadistica.getAsistencias(),
						previa.minutos() + estadistica.getMinutos(), previa.faltas() + estadistica.getFaltas()));
			}
		}

		return acumuladas.values().stream()
				.sorted(Comparator.comparingInt(EstadisticaJugadorDto::puntos).reversed()
						.thenComparingInt(EstadisticaJugadorDto::rebotes).reversed()
						.thenComparingInt(EstadisticaJugadorDto::asistencias).reversed())
				.toList();
	}

	@Transactional
	// Metodo que sirve para registrar una solicitud de contacto para la liga.
	public SolicitudContacto registrarSolicitudContacto(FormularioContactoDto formularioContactoDto) {
		SolicitudContacto solicitudContacto = new SolicitudContacto();
		solicitudContacto.setNombreContacto(formularioContactoDto.getNombreContacto());
		solicitudContacto.setCorreoContacto(formularioContactoDto.getCorreoContacto());
		solicitudContacto.setTelefonoContacto(formularioContactoDto.getTelefonoContacto());
		solicitudContacto.setMensaje(formularioContactoDto.getMensaje());
		return solicitudContactoRepositorio.save(solicitudContacto);
	}

	// Metodo que sirve para construir la fila de estadisticas visible de un jugador en un partido.
	private EstadisticaJugadorPartidoDto construirEstadisticaJugador(Partido partido, Jugador jugador) {
		EstadisticaJugadorPartidoDto estadisticaJugadorPartidoDto = new EstadisticaJugadorPartidoDto();
		estadisticaJugadorPartidoDto.setIdJugador(jugador.getIdJugador());
		estadisticaJugadorPartidoDto.setNombreJugador(jugador.getNombre() + " " + jugador.getApellidos());
		estadisticaJugadorPartidoDto.setNombreEquipo(jugador.getEquipo().getNombre());
		estadisticaJugadorPartidoDto.setDorsal(jugador.getDorsal());

		estadisticaPartidoRepositorio.findByJugadorAndPartido(jugador, partido).ifPresent(estadistica -> {
			estadisticaJugadorPartidoDto.setPuntos(estadistica.getPuntos());
			estadisticaJugadorPartidoDto.setAsistencias(estadistica.getAsistencias());
			estadisticaJugadorPartidoDto.setRebotes(estadistica.getRebotes());
			estadisticaJugadorPartidoDto.setFaltas(estadistica.getFaltas());
			estadisticaJugadorPartidoDto.setMinutos(estadistica.getMinutos());
		});

		return estadisticaJugadorPartidoDto;
	}

	private static class ResumenEquipo {
		private final Equipo equipo;
		private int partidosJugados;
		private int partidosGanados;
		private int puntosFavor;
		private int puntosContra;
		private int rebotes;
		private int asistencias;

		// Metodo que sirve para crear un acumulador de estadisticas por equipo.
		private ResumenEquipo(Equipo equipo) {
			this.equipo = equipo;
		}

		// Metodo que sirve para obtener el numero de partidos ganados.
		private int getPartidosGanados() {
			return partidosGanados;
		}

		// Metodo que sirve para obtener los puntos a favor acumulados del equipo.
		private int getPuntosFavor() {
			return puntosFavor;
		}

		// Metodo que sirve para obtener la diferencia de puntos del equipo.
		private int getDiferenciaPuntos() {
			return puntosFavor - puntosContra;
		}
	}
}
