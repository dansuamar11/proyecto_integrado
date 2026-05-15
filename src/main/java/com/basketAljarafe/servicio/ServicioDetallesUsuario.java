package com.basketAljarafe.servicio;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.basketAljarafe.entidad.Usuario;
import com.basketAljarafe.repositorio.UsuarioRepositorio;

@Service
public class ServicioDetallesUsuario implements UserDetailsService {

	private final UsuarioRepositorio usuarioRepositorio;

	// Metodo que sirve para crear el servicio de autenticacion sobre el repositorio de usuarios.
	public ServicioDetallesUsuario(UsuarioRepositorio usuarioRepositorio) {
		this.usuarioRepositorio = usuarioRepositorio;
	}

	@Override
	// Metodo que sirve para cargar un usuario autenticable por su nombre de acceso.
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepositorio.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("No existe el usuario indicado"));

		return User.withUsername(usuario.getUsername())
				.password(usuario.getPasswordHash())
				.authorities(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombreRol()))
				.disabled(!usuario.isActivo())
				.build();
	}
}
