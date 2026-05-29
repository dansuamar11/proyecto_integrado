import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { Observable, catchError, forkJoin, map, of } from 'rxjs';
import { ActaPartido, Equipo, Jugador, Partido, SolicitudContacto, Usuario } from '../models/api.models';
import { PrivateApiService } from '../services/private-api.service';

export interface EstadoResolucionPrivada<T> {
  datos: T | null;
  error: string;
}

export interface DatosGerente {
  usuarios: Usuario[];
  equipos: Equipo[];
  entrenadores: Usuario[];
  arbitros: Usuario[];
  partidosSinArbitro: Partido[];
  partidosPendientes: Partido[];
  solicitudesContacto: SolicitudContacto[];
}

export interface DatosEntrenador {
  equipo: Equipo | null;
  jugadores: Jugador[];
}

// Metodo que sirve para encapsular la respuesta del resolver privado y evitar que la ruta falle por un error de API.
function resolverPrivadoConEstado<T>(
  peticion: Observable<T>,
  mensajeError: string
): Observable<EstadoResolucionPrivada<T>> {
  return peticion.pipe(
    map((respuesta) => ({
      datos: respuesta,
      error: ''
    })),
    catchError(() =>
      of({
        datos: null,
        error: mensajeError
      })
    )
  );
}

// Resolver que sirve para cargar el panel del gerente antes de activar la ruta.
export const gerenteResolver: ResolveFn<EstadoResolucionPrivada<DatosGerente>> = () => {
  const privateApiService = inject(PrivateApiService);

  return resolverPrivadoConEstado(
    forkJoin({
      usuarios: privateApiService.obtenerUsuarios(),
      equipos: privateApiService.obtenerEquipos(),
      entrenadores: privateApiService.obtenerEntrenadores(),
      arbitros: privateApiService.obtenerArbitros(),
      partidosSinArbitro: privateApiService.obtenerPartidosSinArbitro(),
      partidosPendientes: privateApiService.obtenerPartidosPendientes(),
      solicitudesContacto: privateApiService.obtenerSolicitudesContacto()
    }),
    'No ha sido posible cargar la informacion del gerente.'
  );
};

// Resolver que sirve para cargar el panel del entrenador antes de activar la ruta.
export const entrenadorResolver: ResolveFn<EstadoResolucionPrivada<DatosEntrenador>> = () => {
  const privateApiService = inject(PrivateApiService);

  return resolverPrivadoConEstado(
    forkJoin({
      equipo: privateApiService.obtenerEquipoEntrenador(),
      jugadores: privateApiService.obtenerJugadoresEntrenador()
    }),
    'No ha sido posible cargar la informacion del entrenador.'
  );
};

// Resolver que sirve para cargar el panel del arbitro antes de activar la ruta.
export const arbitroResolver: ResolveFn<EstadoResolucionPrivada<Partido[]>> = () => {
  const privateApiService = inject(PrivateApiService);

  return resolverPrivadoConEstado(
    privateApiService.obtenerPartidosArbitro(),
    'No ha sido posible cargar los partidos del arbitro.'
  );
};

// Resolver que sirve para cargar el acta de un partido antes de activar la ruta.
export const actaArbitroResolver: ResolveFn<EstadoResolucionPrivada<ActaPartido>> = (route) => {
  const privateApiService = inject(PrivateApiService);
  const idPartido = Number(route.paramMap.get('idPartido'));

  return resolverPrivadoConEstado(
    privateApiService.obtenerActaPartido(idPartido),
    'No ha sido posible cargar el acta del partido.'
  );
};

// Resolver que sirve para cargar las solicitudes de contacto antes de activar la ruta.
export const solicitudesContactoResolver: ResolveFn<EstadoResolucionPrivada<SolicitudContacto[]>> = () => {
  const privateApiService = inject(PrivateApiService);

  return resolverPrivadoConEstado(
    privateApiService.obtenerSolicitudesContacto(),
    'No ha sido posible cargar las solicitudes de contacto.'
  );
};
