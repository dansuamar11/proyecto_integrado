package com.basketAljarafe.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basketAljarafe.entidad.Equipo;
import com.basketAljarafe.entidad.Usuario;

public interface EquipoRepositorio extends JpaRepository<Equipo, Integer> {

	// Metodo que sirve para localizar un equipo por su nombre.
	Optional<Equipo> findByNombre(String nombre);

	// Metodo que sirve para localizar el equipo asociado a un entrenador.
	Optional<Equipo> findByEntrenador(Usuario entrenador);
}
