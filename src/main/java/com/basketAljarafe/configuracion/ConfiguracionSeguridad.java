package com.basketAljarafe.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.basketAljarafe.servicio.ServicioDetallesUsuario;

@Configuration
public class ConfiguracionSeguridad {

	private final ServicioDetallesUsuario servicioDetallesUsuario;

	// Metodo que sirve para crear la configuracion de seguridad con el servicio de usuarios.
	public ConfiguracionSeguridad(ServicioDetallesUsuario servicioDetallesUsuario) {
		this.servicioDetallesUsuario = servicioDetallesUsuario;
	}

	@Bean
	// Metodo que sirve para definir la cadena de filtros de seguridad de la aplicacion.
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/", "/clasificacion", "/calendario", "/estadisticas", "/contacto",
								"/contacto/enviar", "/partidos/**", "/css/**", "/api/publico/**",
								"/api/publico/partidos/**", "/api/auth/**")
						.permitAll()
						.requestMatchers("/gerente/**", "/api/gerente/**")
						.hasAnyRole("GERENTE", "ADMIN")
						.requestMatchers("/arbitro/**", "/api/arbitro/**")
						.hasRole("ARBITRO")
						.requestMatchers("/entrenador/**", "/api/entrenador/**")
						.hasRole("ENTRENADOR")
						.anyRequest()
						.authenticated())
				.formLogin(form -> form
						.loginPage("/login")
						.defaultSuccessUrl("/panel", true)
						.permitAll())
				.exceptionHandling(exceptionHandling -> exceptionHandling
						.defaultAuthenticationEntryPointFor(
								new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
								new AntPathRequestMatcher("/api/**")))
				.logout(logout -> logout
						.logoutSuccessUrl("/")
						.permitAll())
				.httpBasic(Customizer.withDefaults());

		return http.build();
	}

	@SuppressWarnings("deprecation")
	@Bean
	// Metodo que sirve para definir el proveedor de autenticacion basado en base de datos.
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(servicioDetallesUsuario);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	// Metodo que sirve para definir el codificador de contrasenas de la aplicacion.
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	// Metodo que sirve para exponer el gestor de autenticacion configurado por Spring Security.
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
