package com.basketAljarafe.servicio;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.basketAljarafe.dto.FormularioJugadorDto;
import com.basketAljarafe.entidad.Equipo;
import com.basketAljarafe.entidad.Jugador;
import com.basketAljarafe.entidad.Usuario;
import com.basketAljarafe.repositorio.EquipoRepositorio;
import com.basketAljarafe.repositorio.JugadorRepositorio;
import com.basketAljarafe.repositorio.UsuarioRepositorio;

@Service
public class ServicioEntrenador {

	private final UsuarioRepositorio usuarioRepositorio;
	private final EquipoRepositorio equipoRepositorio;
	private final JugadorRepositorio jugadorRepositorio;

	// Metodo que sirve para crear el servicio de entrenador con sus repositorios.
	public ServicioEntrenador(UsuarioRepositorio usuarioRepositorio, EquipoRepositorio equipoRepositorio,
			JugadorRepositorio jugadorRepositorio) {
		this.usuarioRepositorio = usuarioRepositorio;
		this.equipoRepositorio = equipoRepositorio;
		this.jugadorRepositorio = jugadorRepositorio;
	}

	@Transactional(readOnly = true)
	// Metodo que sirve para obtener el equipo asociado a un entrenador.
	public Equipo obtenerEquipoDelEntrenador(String username) {
		Usuario entrenador = usuarioRepositorio.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("No existe el usuario indicado"));
		return equipoRepositorio.findByEntrenador(entrenador).orElse(null);
	}

	@Transactional(readOnly = true)
	// Metodo que sirve para obtener los jugadores del equipo del entrenador.
	public List<Jugador> obtenerJugadoresDelEntrenador(String username) {
		Equipo equipo = obtenerEquipoDelEntrenador(username);
		return equipo == null ? List.of() : jugadorRepositorio.findByEquipoOrderByApellidosAscNombreAsc(equipo);
	}

	@Transactional
	// Metodo que sirve para dar de alta a un jugador en el equipo del entrenador.
	public Jugador altaJugador(String username, FormularioJugadorDto formularioJugadorDto) {
		Equipo equipo = obtenerEquipoDelEntrenador(username);
		validarDorsal(equipo, formularioJugadorDto.getDorsal(), null);
		Jugador jugador = new Jugador();
		jugador.setNombre(formularioJugadorDto.getNombre());
		jugador.setApellidos(formularioJugadorDto.getApellidos());
		jugador.setDorsal(formularioJugadorDto.getDorsal());
		jugador.setEquipo(equipo);
		jugador.setActivo(true);
		return jugadorRepositorio.save(jugador);
	}

	@Transactional
	// Metodo que sirve para dar de baja logica a un jugador del equipo del entrenador.
	public Jugador bajaJugador(String username, Integer idJugador) {
		Equipo equipo = obtenerEquipoDelEntrenador(username);
		Jugador jugador = obtenerJugadorDelEquipo(equipo, idJugador);

		jugador.setActivo(false);
		return jugadorRepositorio.save(jugador);
	}

	@Transactional
	// Metodo que sirve para dar de alta logica a un jugador del equipo del entrenador.
	public Jugador altaJugadorExistente(String username, Integer idJugador) {
		Equipo equipo = obtenerEquipoDelEntrenador(username);
		Jugador jugador = obtenerJugadorDelEquipo(equipo, idJugador);
		validarDorsal(equipo, jugador.getDorsal(), jugador.getIdJugador());

		jugador.setActivo(true);
		return jugadorRepositorio.save(jugador);
	}

	// Metodo que sirve para obtener un jugador y validar que pertenece al equipo del entrenador.
	private Jugador obtenerJugadorDelEquipo(Equipo equipo, Integer idJugador) {
		Jugador jugador = jugadorRepositorio.findById(idJugador)
				.orElseThrow(() -> new IllegalArgumentException("No existe el jugador indicado"));

		if (!jugador.getEquipo().getIdEquipo().equals(equipo.getIdEquipo())) {
			throw new IllegalArgumentException("El jugador no pertenece al equipo del entrenador");
		}

		return jugador;
	}

	// Metodo que sirve para validar el dorsal de un jugador dentro del equipo del entrenador.
	private void validarDorsal(Equipo equipo, Integer dorsal, Integer idJugadorActual) {
		if (dorsal == null) {
			throw new IllegalArgumentException("El dorsal es obligatorio");
		}

		if (dorsal < 0 || dorsal > 99 || dorsal == 69 || dorsal == 88) {
			throw new IllegalArgumentException("El dorsal indicado no es valido");
		}

		boolean dorsalOcupado = jugadorRepositorio.findByEquipoOrderByApellidosAscNombreAsc(equipo).stream()
				.anyMatch(jugador -> dorsal.equals(jugador.getDorsal())
						&& (idJugadorActual == null || !jugador.getIdJugador().equals(idJugadorActual)));

		if (dorsalOcupado) {
			throw new IllegalArgumentException("Ya existe un jugador con ese dorsal en el equipo");
		}
	}
}
