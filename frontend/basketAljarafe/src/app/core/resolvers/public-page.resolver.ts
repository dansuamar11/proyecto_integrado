import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { Observable, catchError, map, of } from 'rxjs';
import {
  ActaPartido,
  ClasificacionEquipo,
  EstadisticaJugador,
  Partido,
  ResumenInicio
} from '../models/api.models';
import { PublicApiService } from '../services/public-api.service';

export interface EstadoResolucionPagina<T> {
  datos: T | null;
  error: string;
}

// Metodo que sirve para encapsular la respuesta del resolver y evitar que la ruta falle por un error de API.
function resolverConEstado<T>(peticion: Observable<T>, mensajeError: string): Observable<EstadoResolucionPagina<T>> {
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

// Resolver que sirve para cargar la informacion de inicio antes de activar la ruta.
export const inicioResolver: ResolveFn<EstadoResolucionPagina<ResumenInicio>> = () => {
  const publicApiService = inject(PublicApiService);
  return resolverConEstado(publicApiService.obtenerInicio(), 'No ha sido posible cargar la pagina de inicio.');
};

// Resolver que sirve para cargar la clasificacion antes de activar la ruta.
export const clasificacionResolver: ResolveFn<EstadoResolucionPagina<ClasificacionEquipo[]>> = () => {
  const publicApiService = inject(PublicApiService);
  return resolverConEstado(publicApiService.obtenerClasificacion(), 'No ha sido posible obtener la clasificacion.');
};

// Resolver que sirve para cargar el calendario antes de activar la ruta.
export const calendarioResolver: ResolveFn<EstadoResolucionPagina<Partido[]>> = () => {
  const publicApiService = inject(PublicApiService);
  return resolverConEstado(publicApiService.obtenerCalendario(), 'No ha sido posible obtener el calendario.');
};

// Resolver que sirve para cargar las estadisticas antes de activar la ruta.
export const estadisticasResolver: ResolveFn<EstadoResolucionPagina<EstadisticaJugador[]>> = () => {
  const publicApiService = inject(PublicApiService);
  return resolverConEstado(publicApiService.obtenerEstadisticas(), 'No ha sido posible obtener las estadisticas.');
};

// Resolver que sirve para cargar la ficha publica de un partido antes de activar la ruta.
export const fichaPartidoResolver: ResolveFn<EstadoResolucionPagina<ActaPartido>> = (route) => {
  const publicApiService = inject(PublicApiService);
  const idPartido = Number(route.paramMap.get('idPartido'));
  return resolverConEstado(publicApiService.obtenerFichaPartido(idPartido), 'No ha sido posible cargar la ficha del partido.');
};
