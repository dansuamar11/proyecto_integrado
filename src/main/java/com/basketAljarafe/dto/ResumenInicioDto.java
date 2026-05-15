package com.basketAljarafe.dto;

import java.util.List;

import com.basketAljarafe.entidad.Partido;

public record ResumenInicioDto(
		String nombreLiga,
		String descripcion,
		List<ClasificacionEquipoDto> clasificacionResumida,
		List<Partido> ultimosPartidosJugados) {
}
