import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { SolicitudContacto, convertirFechaApi } from '../../core/models/api.models';
import { EstadoResolucionPrivada } from '../../core/resolvers/private-page.resolver';
import { PrivateApiService } from '../../core/services/private-api.service';

@Component({
  selector: 'app-solicitudes-contacto',
  imports: [CommonModule, RouterLink],
  templateUrl: './solicitudes-contacto.component.html',
  styleUrl: './solicitudes-contacto.component.css'
})
export class SolicitudesContactoComponent {
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly privateApiService = inject(PrivateApiService);
  private readonly datosPagina =
    this.activatedRoute.snapshot.data['datosPagina'] as EstadoResolucionPrivada<SolicitudContacto[]>;

  // Variable que sirve para almacenar las solicitudes visibles en la pagina.
  protected solicitudes = this.datosPagina.datos ?? [];

  // Variable que sirve para reflejar el estado de una eliminacion en curso.
  protected eliminandoId: number | null = null;

  // Variable que sirve para almacenar el mensaje de exito.
  protected mensajeExito = '';

  // Variable que sirve para almacenar el mensaje de error.
  protected error = this.datosPagina.error;

  // Metodo que sirve para obtener la fecha de una solicitud en formato legible.
  protected formatearFecha(fecha: SolicitudContacto['fechaCreacion']): string {
    const fechaConvertida = convertirFechaApi(fecha);

    if (!fechaConvertida) {
      return 'Fecha no disponible';
    }

    return new Intl.DateTimeFormat('es-ES', {
      dateStyle: 'medium',
      timeStyle: 'short'
    }).format(fechaConvertida);
  }

  // Metodo que sirve para eliminar una solicitud tras confirmar la accion.
  protected eliminarSolicitud(solicitud: SolicitudContacto): void {
    if (this.eliminandoId !== null) {
      return;
    }

    const confirmado = window.confirm(
      'Borrar el mensaje lo elimina para siempre. Esta accion no se puede deshacer. ¿Deseas continuar?'
    );

    if (!confirmado) {
      return;
    }

    this.eliminandoId = solicitud.idSolicitud;
    this.mensajeExito = '';
    this.error = '';

    this.privateApiService.eliminarSolicitudContacto(solicitud.idSolicitud).subscribe({
      next: () => {
        window.location.reload();
      },
      error: () => {
        this.error = 'No ha sido posible eliminar la solicitud.';
        this.eliminandoId = null;
      }
    });
  }
}
