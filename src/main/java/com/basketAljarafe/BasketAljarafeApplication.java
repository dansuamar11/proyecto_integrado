package com.basketAljarafe;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.basketAljarafe.entidad.Equipo;
import com.basketAljarafe.entidad.Jugador;
import com.basketAljarafe.entidad.Rol;
import com.basketAljarafe.entidad.Usuario;
import com.basketAljarafe.repositorio.JugadorRepositorio;
import com.basketAljarafe.repositorio.RolRepositorio;
import com.basketAljarafe.repositorio.UsuarioRepositorio;

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

	@Bean
	// Metodo que sirve para garantizar los roles base y el usuario administrador inicial.
	public CommandLineRunner inicializarSeguridadBasica(RolRepositorio rolRepositorio,
			UsuarioRepositorio usuarioRepositorio,
			PasswordEncoder passwordEncoder) {
		return args -> {
			Map<String, Rol> roles = Map.of(
					"ADMIN", obtenerOCrearRol(rolRepositorio, "ADMIN"),
					"GERENTE", obtenerOCrearRol(rolRepositorio, "GERENTE"),
					"ARBITRO", obtenerOCrearRol(rolRepositorio, "ARBITRO"),
					"ENTRENADOR", obtenerOCrearRol(rolRepositorio, "ENTRENADOR"));

			if (usuarioRepositorio.findByUsername("admin").isEmpty()) {
				Usuario administrador = new Usuario();
				administrador.setNombreUsuario("Administrador General");
				administrador.setUsername("admin");
				administrador.setPasswordHash(passwordEncoder.encode("admin123"));
				administrador.setRol(roles.get("ADMIN"));
				administrador.setActivo(true);
				usuarioRepositorio.save(administrador);
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

	// Metodo que sirve para crear un rol base cuando aun no existe en la base de datos.
	private Rol obtenerOCrearRol(RolRepositorio rolRepositorio, String nombreRol) {
		return rolRepositorio.findByNombreRol(nombreRol)
				.orElseGet(() -> rolRepositorio.save(new Rol(nombreRol)));
	}
}
