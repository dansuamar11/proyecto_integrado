package com.basketAljarafe.dto;

import java.time.LocalDateTime;

public class FormularioGenerarPartidoDto {

	private Integer idEquipoLocal;
	private Integer idEquipoVisitante;
	private Integer idArbitro;
	private LocalDateTime fecha;

	public Integer getIdEquipoLocal() {
		return idEquipoLocal;
	}

	public void setIdEquipoLocal(Integer idEquipoLocal) {
		this.idEquipoLocal = idEquipoLocal;
	}

	public Integer getIdEquipoVisitante() {
		return idEquipoVisitante;
	}

	public void setIdEquipoVisitante(Integer idEquipoVisitante) {
		this.idEquipoVisitante = idEquipoVisitante;
	}

	public Integer getIdArbitro() {
		return idArbitro;
	}

	public void setIdArbitro(Integer idArbitro) {
		this.idArbitro = idArbitro;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
}
