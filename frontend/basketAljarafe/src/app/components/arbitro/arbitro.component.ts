import { CommonModule, DatePipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { PasswordChangeModalComponent } from '../password-change-modal/password-change-modal.component';
import { EstadoPartido, FechaApi, Partido, convertirFechaApi } from '../../core/models/api.models';
import { EstadoResolucionPrivada } from '../../core/resolvers/private-page.resolver';

@Component({
  selector: 'app-arbitro',
  imports: [CommonModule, RouterLink, PasswordChangeModalComponent],
  providers: [DatePipe],
  templateUrl: './arbitro.component.html',
  styleUrl: './arbitro.component.css'
})
export class ArbitroComponent {
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly datePipe = inject(DatePipe);
  private readonly datosPagina = this.activatedRoute.snapshot.data['datosPagina'] as EstadoResolucionPrivada<Partido[]>;

  // Variable que sirve para almacenar la lista de partidos asignados al arbitro.
  protected readonly partidos = this.datosPagina.datos ?? [];

  // Variable que sirve para almacenar el mensaje de error.
  protected readonly error = this.datosPagina.error;

  // Variable que sirve para almacenar el mensaje de exito.
  protected mensajeExito = '';

  // Variable que sirve para controlar la visibilidad del modal de cambio de contrasena.
  protected mostrandoModalPassword = false;

  // Variable que sirve para almacenar el equipo seleccionado en el filtro del panel.
  protected equipoSeleccionado = '';

  // Variable que sirve para exponer el listado de equipos disponibles en el selector.
  protected readonly equiposDisponibles = Array.from(
    new Set(
      this.partidos.flatMap((partido) => [
        partido.equipoLocal.nombre,
        partido.equipoVisitante.nombre
      ])
    )
  ).sort((equipoA, equipoB) => equipoA.localeCompare(equipoB));

  // Metodo que sirve para formatear la fecha de un partido.
  protected formatearFecha(partido: Partido): string {
    const fecha = convertirFechaApi(partido.fecha);
    return fecha ? this.datePipe.transform(fecha, 'dd/MM/yyyy HH:mm') || '-' : '-';
  }

  // Metodo que sirve para actualizar el filtro de equipo del panel del arbitro.
  protected actualizarEquipoSeleccionado(evento: Event): void {
    const selector = evento.target as HTMLSelectElement;
    this.equipoSeleccionado = selector.value;
  }

  // Metodo que sirve para obtener los partidos jugados que cumplen el filtro actual.
  protected obtenerPartidosJugados(): Partido[] {
    return this.obtenerPartidosFiltradosPorEstado('JUGADO');
  }

  // Metodo que sirve para obtener los partidos pendientes que cumplen el filtro actual.
  protected obtenerPartidosPendientes(): Partido[] {
    return this.obtenerPartidosFiltradosPorEstado('PENDIENTE');
  }

  // Metodo que sirve para construir el titulo de un partido jugado con el marcador integrado.
  protected obtenerTituloPartidoJugado(partido: Partido): string {
    return `${partido.equipoLocal.nombre} ${partido.puntosLocal ?? 0} - ${partido.puntosVisitante ?? 0} ${partido.equipoVisitante.nombre}`;
  }

  protected abrirModalPassword(): void {
    this.mostrandoModalPassword = true;
  }

  protected cerrarModalPassword(): void {
    this.mostrandoModalPassword = false;
  }

  protected confirmarCambioPassword(mensaje: string): void {
    this.mensajeExito = mensaje;
    this.mostrandoModalPassword = false;
  }

  // Metodo que sirve para filtrar y ordenar los partidos asignados al arbitro por estado.
  private obtenerPartidosFiltradosPorEstado(estado: EstadoPartido): Partido[] {
    const partidosFiltrados = this.partidos.filter((partido) => {
      const coincideEquipo =
        !this.equipoSeleccionado ||
        partido.equipoLocal.nombre === this.equipoSeleccionado ||
        partido.equipoVisitante.nombre === this.equipoSeleccionado;

      return coincideEquipo && partido.estado === estado;
    });

    return partidosFiltrados.sort((partidoA, partidoB) => {
      const fechaPartidoA = this.obtenerMarcaTemporal(partidoA.fecha);
      const fechaPartidoB = this.obtenerMarcaTemporal(partidoB.fecha);

      return estado === 'JUGADO'
        ? fechaPartidoB - fechaPartidoA
        : fechaPartidoA - fechaPartidoB;
    });
  }

  // Metodo que sirve para convertir una fecha de API en una marca temporal numerica.
  private obtenerMarcaTemporal(fecha: FechaApi): number {
    return convertirFechaApi(fecha)?.getTime() ?? 0;
  }
}
