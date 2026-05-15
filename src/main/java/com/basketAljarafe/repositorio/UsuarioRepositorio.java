package com.basketAljarafe.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basketAljarafe.entidad.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {

	// Metodo que sirve para localizar un usuario por su nombre de acceso.
	Optional<Usuario> findByUsername(String username);

	// Metodo que sirve para localizar usuarios de un rol ordenados por nombre.
	List<Usuario> findByRolNombreRolOrderByNombreUsuarioAsc(String nombreRol);
}
