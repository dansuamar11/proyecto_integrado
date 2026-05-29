import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ActaPartido, EstadisticaJugadorPartido } from '../../core/models/api.models';
import { EstadoResolucionPagina } from '../../core/resolvers/public-page.resolver';

@Component({
  selector: 'app-ficha-partido',
  imports: [CommonModule, RouterLink],
  templateUrl: './ficha-partido.component.html',
  styleUrl: './ficha-partido.component.css'
})
export class FichaPartidoComponent {
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly datosPagina = this.activatedRoute.snapshot.data['datosPagina'] as EstadoResolucionPagina<ActaPartido>;

  // Variable que sirve para almacenar la ficha del partido cargada desde la API publica.
  protected readonly fichaPartido = this.datosPagina.datos;

  // Variable que sirve para almacenar el mensaje de error de la vista.
  protected readonly error = this.datosPagina.error;

  // Metodo que sirve para obtener las estadisticas del equipo local.
  protected obtenerEstadisticasLocal(): EstadisticaJugadorPartido[] {
    return this.ordenarPorDorsalDescendente(
      this.fichaPartido?.estadisticasJugadores.filter(
        (estadistica) => estadistica.nombreEquipo === this.fichaPartido?.nombreEquipoLocal
      ) ?? []
    );
  }

  // Metodo que sirve para obtener las estadisticas del equipo visitante.
  protected obtenerEstadisticasVisitante(): EstadisticaJugadorPartido[] {
    return this.ordenarPorDorsalDescendente(
      this.fichaPartido?.estadisticasJugadores.filter(
        (estadistica) => estadistica.nombreEquipo === this.fichaPartido?.nombreEquipoVisitante
      ) ?? []
    );
  }

  // Metodo que sirve para devolver un valor numerico visible evitando nulos en la ficha.
  protected mostrarValor(valor: number | null | undefined): number {
    return valor ?? 0;
  }

  // Metodo que sirve para formatear el dorsal visible en la ficha del partido.
  protected formatearDorsal(dorsal: number | null | undefined): string {
    return `#${this.mostrarValor(dorsal)}`;
  }

  // Metodo que sirve para ordenar una lista de estadisticas por dorsal de forma descendente.
  private ordenarPorDorsalDescendente(estadisticas: EstadisticaJugadorPartido[]): EstadisticaJugadorPartido[] {
    return [...estadisticas].sort((estadisticaA, estadisticaB) => this.mostrarValor(estadisticaA.dorsal) - this.mostrarValor(estadisticaB.dorsal));
  }
}
