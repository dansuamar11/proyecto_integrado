package com.basketAljarafe;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import com.basketAljarafe.entidad.Equipo;
import com.basketAljarafe.entidad.Jugador;
import com.basketAljarafe.repositorio.JugadorRepositorio;

@SpringBootApplication
public class BasketAljarafeApplication {

	// Metodo que sirve para iniciar la aplicacion Spring Boot.
	public static void main(String[] args) {
		SpringApplication.run(BasketAljarafeApplication.class, args);
	}

	@Bean
	// Metodo que sirve para completar y regularizar los dorsales existentes al arrancar la aplicacion.
	public CommandLineRunner inicializarDorsalesJugadores(JugadorRepositorio jugadorRepositorio) {
		return args -> {
			List<Jugador> jugadores = jugadorRepositorio.findAll().stream()
					.sorted(Comparator.comparing((Jugador jugador) -> jugador.getEquipo().getIdEquipo())
							.thenComparing(Jugador::getApellidos)
							.thenComparing(Jugador::getNombre)
							.thenComparing(Jugador::getIdJugador))
					.toList();

			Integer idEquipoActual = null;
			Set<Integer> dorsalesUsados = new HashSet<>();

			for (Jugador jugador : jugadores) {
				Equipo equipo = jugador.getEquipo();

				if (idEquipoActual == null || !idEquipoActual.equals(equipo.getIdEquipo())) {
					idEquipoActual = equipo.getIdEquipo();
					dorsalesUsados = new HashSet<>();
				}

				Integer dorsal = jugador.getDorsal();
				boolean dorsalInvalido = dorsal == null || dorsal < 0 || dorsal > 99 || dorsal == 69 || dorsal == 88
						|| dorsalesUsados.contains(dorsal);

				if (dorsalInvalido) {
					jugador.setDorsal(obtenerSiguienteDorsalDisponible(dorsalesUsados));
					jugadorRepositorio.save(jugador);
				}

				dorsalesUsados.add(jugador.getDorsal());
			}
		};
	}

	// Metodo que sirve para obtener el siguiente dorsal disponible.
	private Integer obtenerSiguienteDorsalDisponible(Set<Integer> dorsalesUsados) {
		return IntStream.rangeClosed(0, 99)
				.filter(dorsal -> dorsal != 69 && dorsal != 88)
				.filter(dorsal -> !dorsalesUsados.contains(dorsal))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("No quedan dorsales disponibles para el equipo"));
	}
}
