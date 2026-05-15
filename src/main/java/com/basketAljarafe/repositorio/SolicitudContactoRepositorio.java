package com.basketAljarafe.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basketAljarafe.entidad.SolicitudContacto;

public interface SolicitudContactoRepositorio extends JpaRepository<SolicitudContacto, Integer> {
}
