package com.basketAljarafe.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basketAljarafe.entidad.Rol;

public interface RolRepositorio extends JpaRepository<Rol, Integer> {

	// Metodo que sirve para localizar un rol por su nombre.
	Optional<Rol> findByNombreRol(String nombreRol);
}
