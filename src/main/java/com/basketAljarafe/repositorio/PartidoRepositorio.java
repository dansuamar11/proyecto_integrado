package com.basketAljarafe.repositorio;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basketAljarafe.entidad.EstadoPartido;
import com.basketAljarafe.entidad.Partido;
import com.basketAljarafe.entidad.Usuario;

public interface PartidoRepositorio extends JpaRepository<Partido, Integer> {

	// Metodo que sirve para obtener todos los partidos ordenados por fecha.
	List<Partido> findAllByOrderByFechaAsc();

	// Metodo que sirve para obtener los partidos posteriores a una fecha.
	List<Partido> findByFechaGreaterThanEqualOrderByFechaAsc(LocalDateTime fecha);

	// Metodo que sirve para obtener los partidos asignados a un arbitro.
	List<Partido> findByArbitroOrderByFechaAsc(Usuario arbitro);

	// Metodo que sirve para obtener los partidos filtrados por estado.
	List<Partido> findByEstadoOrderByFechaAsc(EstadoPartido estado);

	// Metodo que sirve para obtener los partidos pendientes que aun no tienen arbitro asignado.
	List<Partido> findByEstadoAndArbitroIsNullOrderByFechaAsc(EstadoPartido estado);
}
