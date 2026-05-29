import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, retry, timer } from 'rxjs';
import { ActaPartido, ClasificacionEquipo, EstadisticaJugador, FormularioContacto, Partido, ResumenInicio } from '../models/api.models';

@Injectable({
  providedIn: 'root'
})
export class PublicApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/publico';

  // Metodo que sirve para aplicar reintentos amplios en peticiones públicas durante el arranque del entorno.
  private aplicarReintento<T>(observable: Observable<T>): Observable<T> {
    return observable.pipe(
      retry({
        count: 8,
        delay: (_error, indice) => timer(Math.min((indice + 1) * 750, 3000))
      })
    );
  }

  // Metodo que sirve para obtener la informacion pública de inicio.
  obtenerInicio(): Observable<ResumenInicio> {
    return this.aplicarReintento(this.http.get<ResumenInicio>(`${this.baseUrl}/inicio`));
  }

  // Metodo que sirve para obtener la clasificacion completa.
  obtenerClasificacion(): Observable<ClasificacionEquipo[]> {
    return this.aplicarReintento(this.http.get<ClasificacionEquipo[]>(`${this.baseUrl}/clasificacion`));
  }

  // Metodo que sirve para obtener el calendario completo.
  obtenerCalendario(): Observable<Partido[]> {
    return this.aplicarReintento(this.http.get<Partido[]>(`${this.baseUrl}/calendario`));
  }

  // Metodo que sirve para obtener la ficha publica de un partido concreto.
  obtenerFichaPartido(idPartido: number): Observable<ActaPartido> {
    return this.aplicarReintento(this.http.get<ActaPartido>(`${this.baseUrl}/partidos/${idPartido}`));
  }

  // Metodo que sirve para obtener las estadisticas de jugadores.
  obtenerEstadisticas(): Observable<EstadisticaJugador[]> {
    return this.aplicarReintento(this.http.get<EstadisticaJugador[]>(`${this.baseUrl}/estadisticas`));
  }

  // Metodo que sirve para enviar una solicitud de contacto.
  enviarContacto(formulario: FormularioContacto): Observable<unknown> {
    return this.http.post(`${this.baseUrl}/contacto`, formulario);
  }
}
