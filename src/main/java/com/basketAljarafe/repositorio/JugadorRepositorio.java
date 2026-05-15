package com.basketAljarafe.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basketAljarafe.entidad.Equipo;
import com.basketAljarafe.entidad.Jugador;

public interface JugadorRepositorio extends JpaRepository<Jugador, Integer> {

	// Metodo que sirve para obtener los jugadores de un equipo ordenados alfabeticamente.
	List<Jugador> findByEquipoOrderByApellidosAscNombreAsc(Equipo equipo);

	// Metodo que sirve para obtener los jugadores activos ordenados alfabeticamente.
	List<Jugador> findByActivoTrueOrderByApellidosAscNombreAsc();

	// Metodo que sirve para comprobar si un dorsal ya esta ocupado dentro de un equipo.
	boolean existsByEquipoAndDorsal(Equipo equipo, Integer dorsal);
}
