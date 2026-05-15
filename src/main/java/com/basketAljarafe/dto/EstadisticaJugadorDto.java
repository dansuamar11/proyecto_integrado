package com.basketAljarafe.dto;

public record EstadisticaJugadorDto(
		Integer idJugador,
		String nombreJugador,
		Integer dorsal,
		String nombreEquipo,
		Integer puntos,
		Integer rebotes,
		Integer asistencias,
		Integer minutos,
		Integer faltas) {
}
