package com.basketAljarafe.repositorio;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.basketAljarafe.entidad.EstadoPartido;
import com.basketAljarafe.entidad.Partido;
import com.basketAljarafe.entidad.Usuario;

public interface PartidoRepositorio extends JpaRepository<Partido, Integer> {

	// Metodo que sirve para obtener todos los partidos ordenados por fecha.
	List<Partido> findAllByOrderByFechaAsc();

	// Metodo que sirve para obtener los partidos posteriores a una fecha.
	List<Partido> findByFechaGreaterThanEqualOrderByFechaAsc(LocalDateTime fecha);

	// Metodo que sirve para comprobar si ya existe un partido fijado exactamente en una fecha y hora.
	boolean existsByFecha(LocalDateTime fecha);

	// Metodo que sirve para contar si alguno de los equipos ya tiene partido asignado el mismo dia.
	@Query("""
			select count(p)
			from Partido p
			where p.fecha >= :inicioDia
			  and p.fecha < :finDia
			  and (
			    p.equipoLocal.idEquipo in :idsEquipos
			    or p.equipoVisitante.idEquipo in :idsEquipos
			  )
			""")
	long countPartidosMismoDiaParaEquipos(@Param("inicioDia") LocalDateTime inicioDia,
			@Param("finDia") LocalDateTime finDia,
			@Param("idsEquipos") List<Integer> idsEquipos);

	// Metodo que sirve para obtener los partidos asignados a un arbitro.
	List<Partido> findByArbitroOrderByFechaAsc(Usuario arbitro);

	// Metodo que sirve para obtener los partidos filtrados por estado.
	List<Partido> findByEstadoOrderByFechaAsc(EstadoPartido estado);

	// Metodo que sirve para obtener los partidos pendientes que aun no tienen arbitro asignado.
	List<Partido> findByEstadoAndArbitroIsNullOrderByFechaAsc(EstadoPartido estado);
}
