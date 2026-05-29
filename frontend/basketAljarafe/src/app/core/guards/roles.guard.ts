import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map } from 'rxjs';
import { SessionService } from '../services/session.service';

// Guardia que sirve para permitir el acceso solo a usuarios con alguno de los roles indicados.
export function rolesGuard(rolesPermitidos: string[]): CanActivateFn {
  return () => {
    const router = inject(Router);
    const sessionService = inject(SessionService);

    return sessionService.cargarSesion().pipe(
      map((sesion) => {
        if (sesion.autenticado && rolesPermitidos.some((rol) => sesion.roles.includes(rol))) {
          return true;
        }

        return router.createUrlTree(['/404']);
      })
    );
  };
}
