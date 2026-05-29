package com.basketAljarafe.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basketAljarafe.entidad.SolicitudContacto;

public interface SolicitudContactoRepositorio extends JpaRepository<SolicitudContacto, Integer> {

	// Metodo que sirve para obtener las solicitudes de contacto ordenadas de mas reciente a mas antigua.
	java.util.List<SolicitudContacto> findAllByOrderByFechaCreacionDescIdSolicitudDesc();
}
