package com.basketAljarafe.dto;

public record ClasificacionEquipoDto(
		Integer posicion,
		Integer idEquipo,
		String nombreEquipo,
		Integer partidosJugados,
		Integer partidosGanados,
		Integer puntosFavor,
		Integer puntosContra,
		Integer diferenciaPuntos,
		Integer rebotes,
		Integer asistencias) {
}
