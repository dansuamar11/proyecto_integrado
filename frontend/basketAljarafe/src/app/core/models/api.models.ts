// Tipo que sirve para representar los estados posibles de un partido.
export type EstadoPartido = 'PENDIENTE' | 'JUGADO';

// Tipo que sirve para representar una fecha enviada por Spring Boot.
export type FechaApi = string | number[];

// Interfaz que sirve para representar el rol basico de un usuario.
export interface Rol {
  idRol?: number;
  nombreRol: string;
}

// Interfaz que sirve para representar un usuario del sistema.
export interface Usuario {
  idUsuario: number;
  nombreUsuario: string;
  username: string;
  activo: boolean;
  rol: Rol;
}

// Interfaz que sirve para representar un equipo.
export interface Equipo {
  idEquipo: number;
  nombre: string;
  entrenador?: Usuario | null;
}

// Interfaz que sirve para representar un jugador.
export interface Jugador {
  idJugador: number;
  nombre: string;
  apellidos: string;
  dorsal: number | null;
  activo: boolean;
  equipo?: Equipo;
}

// Interfaz que sirve para representar un partido.
export interface Partido {
  idPartido: number;
  equipoLocal: Equipo;
  equipoVisitante: Equipo;
  fecha: FechaApi;
  puntosLocal: number | null;
  puntosVisitante: number | null;
  arbitro?: Usuario | null;
  estado: EstadoPartido;
}

// Interfaz que sirve para representar una fila de clasificacion.
export interface ClasificacionEquipo {
  posicion: number;
  idEquipo: number;
  nombreEquipo: string;
  partidosJugados: number;
  partidosGanados: number;
  puntosFavor: number;
  puntosContra: number;
  diferenciaPuntos: number;
  rebotes: number;
  asistencias: number;
}

// Interfaz que sirve para representar una fila de estadisticas individuales.
export interface EstadisticaJugador {
  idJugador: number;
  nombreJugador: string;
  dorsal: number | null;
  nombreEquipo: string;
  puntos: number;
  rebotes: number;
  asistencias: number;
  minutos: number;
  faltas: number;
}

// Interfaz que sirve para representar la respuesta publica de inicio.
export interface ResumenInicio {
  nombreLiga: string;
  descripcion: string;
  clasificacionResumida: ClasificacionEquipo[];
  ultimosPartidosJugados: Partido[];
}

// Interfaz que sirve para representar una estadistica de jugador dentro del acta.
export interface EstadisticaJugadorPartido {
  idJugador: number;
  nombreJugador: string;
  nombreEquipo: string;
  dorsal: number | null;
  puntos: number | null;
  asistencias: number | null;
  rebotes: number | null;
  faltas: number | null;
  minutos: number | null;
}

// Interfaz que sirve para representar el acta completa de un partido.
export interface ActaPartido {
  idPartido: number;
  nombreEquipoLocal: string;
  nombreEquipoVisitante: string;
  puntosLocal: number | null;
  puntosVisitante: number | null;
  estadisticasJugadores: EstadisticaJugadorPartido[];
}

// Interfaz que sirve para representar el formulario de contacto.
export interface FormularioContacto {
  nombreContacto: string;
  correoContacto: string;
  telefonoContacto: string;
  mensaje: string;
}

// Interfaz que sirve para representar una solicitud recibida desde el formulario de contacto.
export interface SolicitudContacto {
  idSolicitud: number;
  nombreContacto: string;
  correoContacto: string;
  telefonoContacto: string;
  mensaje: string;
  fechaCreacion: FechaApi;
}

// Interfaz que sirve para representar el formulario de usuario.
export interface FormularioUsuario {
  nombreUsuario: string;
  username: string;
  password: string;
  nombreRol: string;
}

// Interfaz que sirve para representar el formulario de equipo.
export interface FormularioEquipo {
  nombre: string;
  idEntrenador: number | null;
}

// Interfaz que sirve para representar el formulario de jugador.
export interface FormularioJugador {
  nombre: string;
  apellidos: string;
  dorsal: number;
}

// Interfaz que sirve para representar el formulario de cambio de entrenador.
export interface FormularioCambioEntrenador {
  idEquipo: number | null;
  idEntrenador: number | null;
}

// Interfaz que sirve para representar el formulario de asignacion de arbitro a un partido.
export interface FormularioAsignacionArbitro {
  idArbitro: number | null;
  idPartido: number | null;
}

// Interfaz que sirve para representar el formulario de generacion de un partido.
export interface FormularioGenerarPartido {
  idEquipoLocal: number | null;
  idEquipoVisitante: number | null;
  idArbitro: number | null;
  fecha: string | null;
}

// Interfaz que sirve para representar la respuesta de autenticacion.
export interface RespuestaSesion {
  authenticated: boolean;
  username: string;
  roles: string[];
}

// Interfaz que sirve para representar el estado de sesion del frontend.
export interface EstadoSesion {
  autenticado: boolean;
  username: string | null;
  roles: string[];
}

// Interfaz que sirve para representar el formulario de login.
export interface FormularioLogin {
  username: string;
  password: string;
}

// Interfaz que sirve para representar el formulario de cambio de contrasena.
export interface FormularioCambioPassword {
  passwordActual: string;
  nuevaPassword: string;
  confirmarNuevaPassword: string;
}

// Funcion que sirve para convertir una fecha JSON del backend en un objeto Date.
export function convertirFechaApi(fecha: FechaApi | null | undefined): Date | null {
  if (!fecha) {
    return null;
  }

  if (typeof fecha === 'string') {
    return new Date(fecha);
  }

  if (Array.isArray(fecha) && fecha.length >= 5) {
    const segundos = fecha[5] ?? 0;
    return new Date(fecha[0], fecha[1] - 1, fecha[2], fecha[3], fecha[4], segundos);
  }

  return null;
}
