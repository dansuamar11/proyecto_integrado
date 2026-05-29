import { computed, inject, Injectable, signal } from '@angular/core';
import { catchError, map, Observable, of, tap } from 'rxjs';
import { EstadoSesion, RespuestaSesion } from '../models/api.models';
import { PrivateApiService } from './private-api.service';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  private readonly privateApiService = inject(PrivateApiService);
  private readonly estadoAnonimo: EstadoSesion = {
    autenticado: false,
    username: null,
    roles: []
  };

  // Variable que sirve para almacenar la informacion de sesion en memoria.
  private readonly estadoInterno = signal<EstadoSesion>(this.estadoAnonimo);

  // Variable que sirve para exponer el estado actual de sesion.
  readonly sesion = computed(() => this.estadoInterno());

  // Metodo que sirve para actualizar el estado local a partir de una respuesta de autenticacion.
  establecerSesionDesdeRespuesta(respuesta: RespuestaSesion): void {
    this.estadoInterno.set({
      autenticado: respuesta.authenticated,
      username: respuesta.authenticated ? respuesta.username : null,
      roles: respuesta.roles
    });
  }

  // Metodo que sirve para cargar la sesion al inicio de la aplicacion.
  inicializarSesion(): void {
    this.cargarSesion().subscribe();
  }

  // Metodo que sirve para consultar la sesion en el backend y actualizar el estado local.
  cargarSesion(): Observable<EstadoSesion> {
    return this.privateApiService.obtenerSesion().pipe(
      map((respuesta) => ({
        autenticado: respuesta.authenticated,
        username: respuesta.authenticated ? respuesta.username : null,
        roles: respuesta.roles
      })),
      tap((estadoSesion) => this.estadoInterno.set(estadoSesion)),
      catchError(() => {
        this.estadoInterno.set(this.estadoAnonimo);
        return of(this.estadoAnonimo);
      })
    );
  }

  // Metodo que sirve para cerrar la sesion en el backend y limpiar el estado local.
  cerrarSesion(): Observable<void> {
    return this.privateApiService.cerrarSesion().pipe(
      tap(() => this.estadoInterno.set(this.estadoAnonimo)),
      catchError(() => {
        this.estadoInterno.set(this.estadoAnonimo);
        return of(void 0);
      })
    );
  }

  // Metodo que sirve para comprobar si la sesion actual contiene alguno de los roles indicados.
  tieneAlgunRol(roles: string[]): boolean {
    return roles.some((rol) => this.estadoInterno().roles.includes(rol));
  }
}
