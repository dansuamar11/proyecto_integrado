import { CommonModule, DatePipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Partido, ResumenInicio, convertirFechaApi } from '../../core/models/api.models';
import { EstadoResolucionPagina } from '../../core/resolvers/public-page.resolver';

@Component({
  selector: 'app-inicio',
  imports: [CommonModule, RouterLink],
  templateUrl: './inicio.component.html',
  styleUrl: './inicio.component.css',
  providers: [DatePipe]
})
export class InicioComponent {
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly datePipe = inject(DatePipe);

  private readonly datosPagina = this.activatedRoute.snapshot.data['datosPagina'] as EstadoResolucionPagina<ResumenInicio>;

  // Variable que sirve para almacenar el resumen de la pagina principal.
  protected readonly resumenInicio = this.datosPagina.datos;

  // Variable que sirve para almacenar los ultimos partidos jugados mostrados en el inicio.
  protected readonly ultimosPartidosJugados = this.resumenInicio?.ultimosPartidosJugados ?? [];

  // Variable que sirve para almacenar el mensaje de error de la pagina.
  protected readonly error = this.datosPagina.error;

  // Metodo que sirve para formatear la fecha visible de un partido.
  protected formatearFecha(partido: Partido): string {
    const fecha = convertirFechaApi(partido.fecha);
    return fecha ? this.datePipe.transform(fecha, 'dd/MM/yyyy HH:mm') || '-' : '-';
  }
}
