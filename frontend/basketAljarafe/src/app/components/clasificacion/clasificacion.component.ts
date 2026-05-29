import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ClasificacionEquipo } from '../../core/models/api.models';
import { EstadoResolucionPagina } from '../../core/resolvers/public-page.resolver';

@Component({
  selector: 'app-clasificacion',
  imports: [CommonModule],
  templateUrl: './clasificacion.component.html',
  styleUrl: './clasificacion.component.css'
})
export class ClasificacionComponent {
  private readonly activatedRoute = inject(ActivatedRoute);

  private readonly datosPagina =
    this.activatedRoute.snapshot.data['datosPagina'] as EstadoResolucionPagina<ClasificacionEquipo[]>;

  // Variable que sirve para almacenar las filas de clasificacion.
  protected readonly clasificacion = this.datosPagina.datos ?? [];

  // Variable que sirve para almacenar el mensaje de error.
  protected readonly error = this.datosPagina.error;
}
