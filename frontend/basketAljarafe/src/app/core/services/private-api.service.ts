import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {
  ActaPartido,
  Equipo,
  FormularioAsignacionArbitro,
  FormularioCambioPassword,
  FormularioCambioEntrenador,
  FormularioEquipo,
  FormularioGenerarPartido,
  FormularioJugador,
  FormularioLogin,
  FormularioUsuario,
  Jugador,
  Partido,
  RespuestaSesion,
  SolicitudContacto,
  Usuario
} from '../models/api.models';

@Injectable({
  providedIn: 'root'
})
export class PrivateApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api';

  // Metodo que sirve para obtener la sesion autenticada actual.
  obtenerSesion(): Observable<RespuestaSesion> {
    return this.http.get<RespuestaSesion>(`${this.baseUrl}/auth/me`, {
      withCredentials: true
    });
  }

  // Metodo que sirve para iniciar sesion contra Spring Security.
  iniciarSesion(formulario: FormularioLogin): Observable<RespuestaSesion> {
    return this.http.post<RespuestaSesion>(`${this.baseUrl}/auth/login`, formulario, {
      withCredentials: true,
    });
  }

  // Metodo que sirve para cerrar la sesion autenticada actual.
  cerrarSesion(): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/auth/logout`, {}, {
      withCredentials: true
    });
  }

  // Metodo que sirve para cambiar la contrasena del usuario autenticado.
  cambiarPassword(formulario: FormularioCambioPassword): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/auth/cambiar-password`, formulario, {
      withCredentials: true
    });
  }

  // Metodo que sirve para obtener los usuarios del gerente.
  obtenerUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.baseUrl}/gerente/usuarios`, {
      withCredentials: true
    });
  }

  // Metodo que sirve para crear un usuario desde el panel del gerente.
  crearUsuario(formulario: FormularioUsuario): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.baseUrl}/gerente/usuarios`, formulario, {
      withCredentials: true
    });
  }

  // Metodo que sirve para desactivar un usuario desde el panel del gerente.
  desactivarUsuario(idUsuario: number): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.baseUrl}/gerente/usuarios/${idUsuario}/desactivar`, {}, {
      withCredentials: true
    });
  }

  // Metodo que sirve para reactivar un usuario desde el panel del gerente.
  activarUsuario(idUsuario: number): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.baseUrl}/gerente/usuarios/${idUsuario}/activar`, {}, {
      withCredentials: true
    });
  }

  // Metodo que sirve para obtener los equipos del gerente.
  obtenerEquipos(): Observable<Equipo[]> {
    return this.http.get<Equipo[]>(`${this.baseUrl}/gerente/equipos`, {
      withCredentials: true
    });
  }

  // Metodo que sirve para obtener los entrenadores disponibles.
  obtenerEntrenadores(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.baseUrl}/gerente/entrenadores`, {
      withCredentials: true
    });
  }

  // Metodo que sirve para obtener los arbitros activos disponibles.
  obtenerArbitros(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.baseUrl}/gerente/arbitros`, {
      withCredentials: true
    });
  }

  // Metodo que sirve para obtener los partidos sin arbitro asignado.
  obtenerPartidosSinArbitro(): Observable<Partido[]> {
    return this.http.get<Partido[]>(`${this.baseUrl}/gerente/partidos-sin-arbitro`, {
      withCredentials: true
    });
  }

  // Metodo que sirve para obtener todos los partidos pendientes para asignar o cambiar arbitro.
  obtenerPartidosPendientes(): Observable<Partido[]> {
    return this.http.get<Partido[]>(`${this.baseUrl}/gerente/partidos-pendientes`, {
      withCredentials: true
    });
  }

  // Metodo que sirve para obtener las solicitudes llegadas desde la pagina de contacto.
  obtenerSolicitudesContacto(): Observable<SolicitudContacto[]> {
    return this.http.get<SolicitudContacto[]>(`${this.baseUrl}/gerente/solicitudes-contacto`, {
      withCredentials: true
    });
  }

  // Metodo que sirve para crear un equipo desde el panel del gerente.
  crearEquipo(formulario: FormularioEquipo): Observable<Equipo> {
    return this.http.post<Equipo>(`${this.baseUrl}/gerente/equipos`, formulario, {
      withCredentials: true
    });
  }

  // Metodo que sirve para cambiar el entrenador de un equipo.
  cambiarEntrenador(formulario: FormularioCambioEntrenador): Observable<Equipo> {
    return this.http.post<Equipo>(`${this.baseUrl}/gerente/equipos/cambiar-entrenador`, formulario, {
      withCredentials: true
    });
  }

  // Metodo que sirve para asignar un arbitro a un partido.
  asignarArbitroAPartido(formulario: FormularioAsignacionArbitro): Observable<Partido> {
    return this.http.post<Partido>(`${this.baseUrl}/gerente/partidos/asignar-arbitro`, formulario, {
      withCredentials: true
    });
  }

  // Metodo que sirve para eliminar una solicitud de contacto del panel de gestion.
  eliminarSolicitudContacto(idSolicitud: number): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/gerente/solicitudes-contacto/${idSolicitud}/eliminar`, {}, {
      withCredentials: true
    });
  }

  // Metodo que sirve para obtener el equipo del entrenador autenticado.
  obtenerEquipoEntrenador(): Observable<Equipo> {
    return this.http.get<Equipo>(`${this.baseUrl}/entrenador/equipo`, {
      withCredentials: true
    });
  }

  // Metodo que sirve para obtener los jugadores del entrenador autenticado.
  obtenerJugadoresEntrenador(): Observable<Jugador[]> {
    return this.http.get<Jugador[]>(`${this.baseUrl}/entrenador/jugadores`, {
      withCredentials: true
    });
  }

  // Metodo que sirve para crear un jugador desde el panel del entrenador.
  crearJugador(formulario: FormularioJugador): Observable<Jugador> {
    return this.http.post<Jugador>(`${this.baseUrl}/entrenador/jugadores`, formulario, {
      withCredentials: true
    });
  }

  // Metodo que sirve para dar de baja un jugador desde el panel del entrenador.
  darBajaJugador(idJugador: number): Observable<Jugador> {
    return this.http.post<Jugador>(`${this.baseUrl}/entrenador/jugadores/${idJugador}/baja`, {}, {
      withCredentials: true
    });
  }

  // Metodo que sirve para dar de alta un jugador desde el panel del entrenador.
  darAltaJugador(idJugador: number): Observable<Jugador> {
    return this.http.post<Jugador>(`${this.baseUrl}/entrenador/jugadores/${idJugador}/alta`, {}, {
      withCredentials: true
    });
  }

  // Metodo que sirve para obtener los partidos del arbitro autenticado.
  obtenerPartidosArbitro(): Observable<Partido[]> {
    return this.http.get<Partido[]>(`${this.baseUrl}/arbitro/partidos`, {
      withCredentials: true
    });
  }

  // Metodo que sirve para obtener el acta de un partido.
  obtenerActaPartido(idPartido: number): Observable<ActaPartido> {
    return this.http.get<ActaPartido>(`${this.baseUrl}/arbitro/partidos/${idPartido}/acta`, {
      withCredentials: true
    });
  }

  // Metodo que sirve para guardar el acta completa de un partido.
  guardarActaPartido(idPartido: number, actaPartido: ActaPartido): Observable<Partido> {
    return this.http.post<Partido>(`${this.baseUrl}/arbitro/partidos/${idPartido}/acta`, actaPartido, {
      withCredentials: true
    });
  }

  // Metodo que sirve para crear un nuevo partido desde el panel del gerente.
  generarPartido(formulario: FormularioGenerarPartido): Observable<Partido> {
    return this.http.post<Partido>(`${this.baseUrl}/gerente/partidos`, formulario, {
      withCredentials: true
    });
  }
}
