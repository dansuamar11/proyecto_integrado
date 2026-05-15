package com.basketAljarafe.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basketAljarafe.entidad.EstadisticaPartido;
import com.basketAljarafe.entidad.Jugador;
import com.basketAljarafe.entidad.Partido;

public interface EstadisticaPartidoRepositorio extends JpaRepository<EstadisticaPartido, Integer> {

	// Metodo que sirve para obtener las estadisticas asociadas a un partido.
	List<EstadisticaPartido> findByPartidoIdPartido(Integer idPartido);

	// Metodo que sirve para localizar la estadistica de un jugador en un partido concreto.
	Optional<EstadisticaPartido> findByJugadorAndPartido(Jugador jugador, Partido partido);
}
