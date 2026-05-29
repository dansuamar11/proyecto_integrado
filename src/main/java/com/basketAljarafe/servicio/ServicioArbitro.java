package com.basketAljarafe.servicio;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.basketAljarafe.dto.ActaPartidoDto;
import com.basketAljarafe.dto.EstadisticaJugadorPartidoDto;
import com.basketAljarafe.entidad.EstadisticaPartido;
import com.basketAljarafe.entidad.EstadoPartido;
import com.basketAljarafe.entidad.Jugador;
import com.basketAljarafe.entidad.Partido;
import com.basketAljarafe.entidad.Usuario;
import com.basketAljarafe.repositorio.EstadisticaPartidoRepositorio;
import com.basketAljarafe.repositorio.JugadorRepositorio;
import com.basketAljarafe.repositorio.PartidoRepositorio;
import com.basketAljarafe.repositorio.UsuarioRepositorio;

@Service
public class ServicioArbitro {

	private final UsuarioRepositorio usuarioRepositorio;
	private final PartidoRepositorio partidoRepositorio;
	private final JugadorRepositorio jugadorRepositorio;
	private final EstadisticaPartidoRepositorio estadisticaPartidoRepositorio;

	// Metodo que sirve para crear el servicio de arbitro con sus repositorios.
	public ServicioArbitro(UsuarioRepositorio usuarioRepositorio, PartidoRepositorio partidoRepositorio,
			JugadorRepositorio jugadorRepositorio, EstadisticaPartidoRepositorio estadisticaPartidoRepositorio) {
		this.usuarioRepositorio = usuarioRepositorio;
		this.partidoRepositorio = partidoRepositorio;
		this.jugadorRepositorio = jugadorRepositorio;
		this.estadisticaPartidoRepositorio = estadisticaPartidoRepositorio;
	}

	@Transactional(readOnly = true)
	// Metodo que sirve para obtener los partidos asignados a un arbitro.
	public List<Partido> obtenerPartidosDelArbitro(String username) {
		Usuario arbitro = usuarioRepositorio.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("No existe el arbitro indicado"));
		return partidoRepositorio.findByArbitroOrderByFechaAsc(arbitro);
	}

	@Transactional(readOnly = true)
	// Metodo que sirve para construir la ficha completa de un partido asignado a un arbitro.
	public ActaPartidoDto obtenerActaPartido(String username, Integer idPartido) {
		Partido partido = obtenerPartidoValidado(username, idPartido);
		ActaPartidoDto actaPartidoDto = new ActaPartidoDto();
		actaPartidoDto.setIdPartido(partido.getIdPartido());
		actaPartidoDto.setNombreEquipoLocal(partido.getEquipoLocal().getNombre());
		actaPartidoDto.setNombreEquipoVisitante(partido.getEquipoVisitante().getNombre());
		actaPartidoDto.setPuntosLocal(partido.getPuntosLocal());
		actaPartidoDto.setPuntosVisitante(partido.getPuntosVisitante());
		actaPartidoDto.setEstadisticasJugadores(obtenerJugadoresVisibles(partido).stream()
				.map(jugador -> construirEstadisticaJugador(partido, jugador))
				.collect(Collectors.toList()));
		return actaPartidoDto;
	}

	@Transactional
	// Metodo que sirve para registrar el acta completa de un partido.
	public Partido registrarActa(String username, Integer idPartido, ActaPartidoDto actaPartidoDto) {
		Partido partido = obtenerPartidoValidado(username, idPartido);
		int puntosLocal = 0;
		int puntosVisitante = 0;

		for (EstadisticaJugadorPartidoDto estadisticaDto : actaPartidoDto.getEstadisticasJugadores()) {
			Jugador jugador = jugadorRepositorio.findById(estadisticaDto.getIdJugador())
					.orElseThrow(() -> new IllegalArgumentException("No existe el jugador indicado"));
			validarJugadorPermitidoEnActa(partido, jugador);

			EstadisticaPartido estadisticaPartido = estadisticaPartidoRepositorio.findByJugadorAndPartido(jugador, partido)
					.orElse(new EstadisticaPartido());
			estadisticaPartido.setJugador(jugador);
			estadisticaPartido.setPartido(partido);
			estadisticaPartido.setPuntos(valorSeguro(estadisticaDto.getPuntos()));
			estadisticaPartido.setAsistencias(valorSeguro(estadisticaDto.getAsistencias()));
			estadisticaPartido.setRebotes(valorSeguro(estadisticaDto.getRebotes()));
			estadisticaPartido.setFaltas(valorSeguro(estadisticaDto.getFaltas()));
			estadisticaPartido.setMinutos(valorSeguro(estadisticaDto.getMinutos()));
			estadisticaPartidoRepositorio.save(estadisticaPartido);

			if (jugador.getEquipo().getIdEquipo().equals(partido.getEquipoLocal().getIdEquipo())) {
				puntosLocal += estadisticaPartido.getPuntos();
			} else if (jugador.getEquipo().getIdEquipo().equals(partido.getEquipoVisitante().getIdEquipo())) {
				puntosVisitante += estadisticaPartido.getPuntos();
			}
		}

		partido.setPuntosLocal(puntosLocal);
		partido.setPuntosVisitante(puntosVisitante);
		partido.setEstado(EstadoPartido.JUGADO);
		partidoRepositorio.save(partido);

		return partido;
	}

	// Metodo que sirve para validar que un arbitro puede abrir o editar un partido concreto.
	private Partido obtenerPartidoValidado(String username, Integer idPartido) {
		Usuario arbitro = usuarioRepositorio.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("No existe el arbitro indicado"));
		Partido partido = partidoRepositorio.findById(idPartido)
				.orElseThrow(() -> new IllegalArgumentException("No existe el partido indicado"));

		if (partido.getArbitro() == null || !partido.getArbitro().getIdUsuario().equals(arbitro.getIdUsuario())) {
			throw new IllegalArgumentException("El arbitro no puede editar este partido");
		}

		return partido;
	}

	// Metodo que sirve para construir la fila de estadisticas editable de un jugador en un partido.
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

	// Metodo que sirve para devolver los jugadores visibles segun el estado del partido.
	private List<Jugador> obtenerJugadoresVisibles(Partido partido) {
		if (partido.getEstado() == EstadoPartido.JUGADO) {
			return estadisticaPartidoRepositorio.findByPartidoIdPartido(partido.getIdPartido()).stream()
					.map(EstadisticaPartido::getJugador)
					.distinct()
					.sorted((jugadorA, jugadorB) -> Integer.compare(valorSeguro(jugadorA.getDorsal()),
							valorSeguro(jugadorB.getDorsal())))
					.toList();
		}

		return Stream.concat(
				partido.getEquipoLocal().getJugadores().stream(),
				partido.getEquipoVisitante().getJugadores().stream())
				.filter(Jugador::isActivo)
				.sorted((jugadorA, jugadorB) -> Integer.compare(valorSeguro(jugadorA.getDorsal()),
						valorSeguro(jugadorB.getDorsal())))
				.toList();
	}

	// Metodo que sirve para validar si un jugador puede formar parte del acta del partido.
	private void validarJugadorPermitidoEnActa(Partido partido, Jugador jugador) {
		boolean perteneceAlPartido = jugador.getEquipo().getIdEquipo().equals(partido.getEquipoLocal().getIdEquipo())
				|| jugador.getEquipo().getIdEquipo().equals(partido.getEquipoVisitante().getIdEquipo());

		if (!perteneceAlPartido) {
			throw new IllegalArgumentException("El jugador no pertenece a ninguno de los equipos del partido");
		}

		boolean yaTeniaEstadisticas = estadisticaPartidoRepositorio.findByJugadorAndPartido(jugador, partido).isPresent();
		if (!jugador.isActivo() && !yaTeniaEstadisticas) {
			throw new IllegalArgumentException("El jugador indicado no puede anadirse al acta de este partido");
		}
	}

	// Metodo que sirve para evitar valores nulos en los datos numericos del acta.
	private Integer valorSeguro(Integer valor) {
		return valor == null ? 0 : valor;
	}
}
