package com.basketAljarafe.servicio;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.basketAljarafe.dto.FormularioCambioPasswordDto;
import com.basketAljarafe.entidad.Usuario;
import com.basketAljarafe.repositorio.UsuarioRepositorio;

@Service
public class ServicioCuenta {

	private final UsuarioRepositorio usuarioRepositorio;
	private final PasswordEncoder passwordEncoder;

	public ServicioCuenta(UsuarioRepositorio usuarioRepositorio, PasswordEncoder passwordEncoder) {
		this.usuarioRepositorio = usuarioRepositorio;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void cambiarPassword(String username, FormularioCambioPasswordDto formularioCambioPasswordDto) {
		Usuario usuario = usuarioRepositorio.findByUsername(username)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el usuario indicado."));

		String passwordActual = normalizarTexto(formularioCambioPasswordDto.getPasswordActual());
		String nuevaPassword = normalizarTexto(formularioCambioPasswordDto.getNuevaPassword());
		String confirmarNuevaPassword = normalizarTexto(formularioCambioPasswordDto.getConfirmarNuevaPassword());

		if (passwordActual == null || nuevaPassword == null || confirmarNuevaPassword == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes completar todos los campos.");
		}

		if (!passwordEncoder.matches(passwordActual, usuario.getPasswordHash())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña actual no es correcta.");
		}

		if (!nuevaPassword.equals(confirmarNuevaPassword)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las nuevas contraseñas no coinciden.");
		}

		if (nuevaPassword.length() < 6) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"La nueva contraseña debe tener al menos 6 caracteres.");
		}

		usuario.setPasswordHash(passwordEncoder.encode(nuevaPassword));
		usuarioRepositorio.save(usuario);
	}

	private String normalizarTexto(String texto) {
		if (texto == null) {
			return null;
		}

		String textoNormalizado = texto.trim();
		return textoNormalizado.isEmpty() ? null : textoNormalizado;
	}
}
