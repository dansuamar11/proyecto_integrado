import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EstadisticaJugador } from '../../core/models/api.models';
import { EstadoResolucionPagina } from '../../core/resolvers/public-page.resolver';

type CategoriaEstadistica = 'puntos' | 'rebotes' | 'asistencias' | 'minutos' | 'fairPlay';

@Component({
  selector: 'app-estadisticas',
  imports: [CommonModule],
  templateUrl: './estadisticas.component.html',
  styleUrl: './estadisticas.component.css'
})
export class EstadisticasComponent {
  private readonly activatedRoute = inject(ActivatedRoute);

  private readonly datosPagina =
    this.activatedRoute.snapshot.data['datosPagina'] as EstadoResolucionPagina<EstadisticaJugador[]>;

  // Variable que sirve para almacenar las estadisticas individuales.
  protected readonly estadisticas = this.datosPagina.datos ?? [];

  // Variable que sirve para almacenar el mensaje de error.
  protected readonly error = this.datosPagina.error;

  // Variable que sirve para almacenar la categoria activa de la clasificacion estadistica.
  protected categoriaSeleccionada: CategoriaEstadistica = 'puntos';

  // Variable que sirve para definir las categorias disponibles del panel de estadisticas.
  protected readonly categorias = [
    { clave: 'puntos' as const, etiqueta: '🏀 Puntos' },
    { clave: 'rebotes' as const, etiqueta: '🛡️ Rebotes' },
    { clave: 'asistencias' as const, etiqueta: '🎯 Asistencias' },
    { clave: 'minutos' as const, etiqueta: '⏱️ Minutos' },
    { clave: 'fairPlay' as const, etiqueta: '🤝 FairPlay' }
  ];

  // Metodo que sirve para actualizar la categoria visible del ranking estadistico.
  protected seleccionarCategoria(categoria: CategoriaEstadistica): void {
    this.categoriaSeleccionada = categoria;
  }

  // Metodo que sirve para obtener el top 10 de jugadores segun la categoria activa.
  protected obtenerTopJugadores(): EstadisticaJugador[] {
    return [...this.estadisticas]
      .sort((estadisticaA, estadisticaB) => this.compararEstadisticas(estadisticaA, estadisticaB))
      .slice(0, 10);
  }

  // Metodo que sirve para obtener el valor visible de una estadistica segun la categoria activa.
  protected obtenerValorCategoria(estadistica: EstadisticaJugador): number {
    switch (this.categoriaSeleccionada) {
      case 'puntos':
        return estadistica.puntos;
      case 'rebotes':
        return estadistica.rebotes;
      case 'asistencias':
        return estadistica.asistencias;
      case 'minutos':
        return estadistica.minutos;
      case 'fairPlay':
        return estadistica.faltas;
    }
  }

  // Metodo que sirve para formatear el dorsal visible del ranking estadistico.
  protected formatearDorsal(dorsal: number | null | undefined): string {
    return `#${dorsal ?? 0}`;
  }

  // Metodo que sirve para obtener el encabezado visible de la columna principal.
  protected obtenerEtiquetaColumna(): string {
    switch (this.categoriaSeleccionada) {
      case 'puntos':
        return 'Puntos';
      case 'rebotes':
        return 'Rebotes';
      case 'asistencias':
        return 'Asistencias';
      case 'minutos':
        return 'Minutos';
      case 'fairPlay':
        return 'Faltas';
    }
  }

  // Metodo que sirve para comparar dos jugadores segun la categoria activa.
  private compararEstadisticas(estadisticaA: EstadisticaJugador, estadisticaB: EstadisticaJugador): number {
    if (this.categoriaSeleccionada === 'fairPlay') {
      return (
        estadisticaA.faltas - estadisticaB.faltas ||
        estadisticaB.minutos - estadisticaA.minutos ||
        estadisticaA.nombreJugador.localeCompare(estadisticaB.nombreJugador)
      );
    }

    return (
      this.obtenerValorCategoria(estadisticaB) - this.obtenerValorCategoria(estadisticaA) ||
      estadisticaB.puntos - estadisticaA.puntos ||
      estadisticaA.nombreJugador.localeCompare(estadisticaB.nombreJugador)
    );
  }
}
