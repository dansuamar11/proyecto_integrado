import { CommonModule, DatePipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FechaApi, Partido, convertirFechaApi } from '../../core/models/api.models';
import { EstadoResolucionPagina } from '../../core/resolvers/public-page.resolver';

@Component({
  selector: 'app-calendario',
  imports: [CommonModule, RouterLink],
  providers: [DatePipe],
  templateUrl: './calendario.component.html',
  styleUrl: './calendario.component.css'
})
export class CalendarioComponent {
  private readonly tamanoPagina = 5;
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly datePipe = inject(DatePipe);

  private readonly datosPagina = this.activatedRoute.snapshot.data['datosPagina'] as EstadoResolucionPagina<Partido[]>;

  // Variable que sirve para almacenar la lista de partidos del calendario.
  protected readonly partidos = this.datosPagina.datos ?? [];

  // Variable que sirve para almacenar el mensaje de error.
  protected readonly error = this.datosPagina.error;

  // Variable que sirve para almacenar el equipo seleccionado en el filtro del calendario.
  protected equipoSeleccionado = '';

  // Variable que sirve para controlar la pagina visible de partidos jugados.
  protected paginaJugados = 0;

  // Variable que sirve para controlar la pagina visible de partidos pendientes.
  protected paginaPendientes = 0;

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

  // Metodo que sirve para obtener la clase visual del estado del partido.
  protected obtenerClaseEstado(estado: string): string {
    return estado === 'JUGADO' ? 'status-pill status-pill--played' : 'status-pill status-pill--pending';
  }

  // Metodo que sirve para actualizar el filtro de equipo del calendario.
  protected actualizarEquipoSeleccionado(evento: Event): void {
    const selector = evento.target as HTMLSelectElement;
    this.equipoSeleccionado = selector.value;
    this.paginaJugados = 0;
    this.paginaPendientes = 0;
  }

  // Metodo que sirve para obtener los partidos jugados de la pagina actual.
  protected obtenerPartidosJugadosPagina(): Partido[] {
    return this.obtenerPartidosPaginados('JUGADO', this.paginaJugados);
  }

  // Metodo que sirve para obtener los partidos pendientes de la pagina actual.
  protected obtenerPartidosPendientesPagina(): Partido[] {
    return this.obtenerPartidosPaginados('PENDIENTE', this.paginaPendientes);
  }

  // Metodo que sirve para avanzar o retroceder la pagina de partidos jugados.
  protected cambiarPaginaJugados(direccion: number): void {
    const nuevaPagina = this.paginaJugados + direccion;

    if (nuevaPagina >= 0 && nuevaPagina < this.obtenerTotalPaginas('JUGADO')) {
      this.paginaJugados = nuevaPagina;
    }
  }

  // Metodo que sirve para avanzar o retroceder la pagina de partidos pendientes.
  protected cambiarPaginaPendientes(direccion: number): void {
    const nuevaPagina = this.paginaPendientes + direccion;

    if (nuevaPagina >= 0 && nuevaPagina < this.obtenerTotalPaginas('PENDIENTE')) {
      this.paginaPendientes = nuevaPagina;
    }
  }

  // Metodo que sirve para comprobar si existe una pagina anterior de partidos jugados.
  protected puedeRetrocederJugados(): boolean {
    return this.paginaJugados > 0;
  }

  // Metodo que sirve para comprobar si existe una pagina siguiente de partidos jugados.
  protected puedeAvanzarJugados(): boolean {
    return this.paginaJugados < this.obtenerTotalPaginas('JUGADO') - 1;
  }

  // Metodo que sirve para comprobar si existe una pagina anterior de partidos pendientes.
  protected puedeRetrocederPendientes(): boolean {
    return this.paginaPendientes > 0;
  }

  // Metodo que sirve para comprobar si existe una pagina siguiente de partidos pendientes.
  protected puedeAvanzarPendientes(): boolean {
    return this.paginaPendientes < this.obtenerTotalPaginas('PENDIENTE') - 1;
  }

  // Metodo que sirve para obtener el texto del rango actual de partidos jugados.
  protected obtenerResumenJugados(): string {
    return this.obtenerResumenPaginacion('JUGADO', this.paginaJugados);
  }

  // Metodo que sirve para obtener el texto del rango actual de partidos pendientes.
  protected obtenerResumenPendientes(): string {
    return this.obtenerResumenPaginacion('PENDIENTE', this.paginaPendientes);
  }

  // Metodo que sirve para construir el resumen visible de una paginacion.
  private obtenerResumenPaginacion(estado: 'JUGADO' | 'PENDIENTE', pagina: number): string {
    const totalPartidos = this.obtenerPartidosFiltradosPorEstado(estado).length;

    if (totalPartidos === 0) {
      return '0 resultados';
    }

    const inicio = pagina * this.tamanoPagina + 1;
    const fin = Math.min(inicio + this.tamanoPagina - 1, totalPartidos);
    return `${inicio}-${fin} de ${totalPartidos}`;
  }

  // Metodo que sirve para calcular el numero total de paginas de un estado concreto.
  private obtenerTotalPaginas(estado: 'JUGADO' | 'PENDIENTE'): number {
    const totalPartidos = this.obtenerPartidosFiltradosPorEstado(estado).length;
    return Math.max(1, Math.ceil(totalPartidos / this.tamanoPagina));
  }

  // Metodo que sirve para obtener la lista paginada de partidos de un estado concreto.
  private obtenerPartidosPaginados(estado: 'JUGADO' | 'PENDIENTE', pagina: number): Partido[] {
    const partidosFiltrados = this.obtenerPartidosFiltradosPorEstado(estado);
    const inicio = pagina * this.tamanoPagina;
    return partidosFiltrados.slice(inicio, inicio + this.tamanoPagina);
  }

  // Metodo que sirve para filtrar y ordenar los partidos del calendario por estado.
  private obtenerPartidosFiltradosPorEstado(estado: 'JUGADO' | 'PENDIENTE'): Partido[] {
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
