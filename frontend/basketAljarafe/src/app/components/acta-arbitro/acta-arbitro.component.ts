import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ActaPartido, EstadisticaJugadorPartido } from '../../core/models/api.models';
import { EstadoResolucionPrivada } from '../../core/resolvers/private-page.resolver';
import { PrivateApiService } from '../../core/services/private-api.service';

@Component({
  selector: 'app-acta-arbitro',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './acta-arbitro.component.html',
  styleUrl: './acta-arbitro.component.css'
})
export class ActaArbitroComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly privateApiService = inject(PrivateApiService);
  private readonly datosPagina = this.activatedRoute.snapshot.data['datosPagina'] as EstadoResolucionPrivada<ActaPartido>;

  // Variable que sirve para almacenar la informacion del acta cargada desde la API.
  protected actaPartido: ActaPartido | null = this.datosPagina.datos;

  // Variable que sirve para almacenar el mensaje de exito.
  protected mensajeExito = '';

  // Variable que sirve para almacenar el mensaje de error.
  protected error = this.datosPagina.error;

  // Variable que sirve para representar el formulario completo del acta.
  protected readonly formularioActa = this.formBuilder.group({
    idPartido: [0, Validators.required],
    nombreEquipoLocal: [''],
    nombreEquipoVisitante: [''],
    puntosLocal: [0],
    puntosVisitante: [0],
    estadisticasJugadores: this.formBuilder.array<FormGroup>([])
  });

  // Metodo que sirve para exponer el array de estadisticas del formulario.
  protected get estadisticasJugadores(): FormArray<FormGroup> {
    return this.formularioActa.get('estadisticasJugadores') as FormArray<FormGroup>;
  }

  // Metodo que sirve para inicializar el formulario con los datos resueltos de la ruta.
  constructor() {
    if (this.actaPartido) {
      this.rellenarFormulario(this.actaPartido);
    }
  }

  // Metodo que sirve para calcular la suma de puntos del equipo local.
  protected totalLocal(): number {
    return this.sumarPuntosPorEquipo(this.formularioActa.getRawValue().nombreEquipoLocal || '');
  }

  // Metodo que sirve para calcular la suma de puntos del equipo visitante.
  protected totalVisitante(): number {
    return this.sumarPuntosPorEquipo(this.formularioActa.getRawValue().nombreEquipoVisitante || '');
  }

  // Metodo que sirve para obtener las estadisticas editables del equipo local.
  protected obtenerEstadisticasLocal(): FormGroup[] {
    return this.obtenerEstadisticasPorEquipo(this.formularioActa.getRawValue().nombreEquipoLocal || '');
  }

  // Metodo que sirve para obtener las estadisticas editables del equipo visitante.
  protected obtenerEstadisticasVisitante(): FormGroup[] {
    return this.obtenerEstadisticasPorEquipo(this.formularioActa.getRawValue().nombreEquipoVisitante || '');
  }

  // Metodo que sirve para obtener el indice real de una estadistica dentro del FormArray.
  protected obtenerIndiceEstadistica(estadistica: FormGroup): number {
    return this.estadisticasJugadores.controls.indexOf(estadistica);
  }

  // Metodo que sirve para formatear el dorsal visible de un jugador.
  protected formatearDorsal(dorsal: number | null | undefined): string {
    return `#${dorsal ?? 0}`;
  }

  // Metodo que sirve para guardar el acta completa del partido.
  protected guardarActa(): void {
    if (this.formularioActa.invalid) {
      this.formularioActa.markAllAsTouched();
      this.error = 'Revisa el acta: no se permiten valores negativos y las faltas no pueden superar 5.';
      return;
    }

    const formulario = this.formularioActa.getRawValue();
    const actaPartido: ActaPartido = {
      idPartido: formulario.idPartido || 0,
      nombreEquipoLocal: formulario.nombreEquipoLocal || '',
      nombreEquipoVisitante: formulario.nombreEquipoVisitante || '',
      puntosLocal: this.totalLocal(),
      puntosVisitante: this.totalVisitante(),
      estadisticasJugadores: formulario.estadisticasJugadores as EstadisticaJugadorPartido[]
    };

    this.privateApiService.guardarActaPartido(actaPartido.idPartido, actaPartido).subscribe({
      next: () => {
        this.mensajeExito = 'Acta registrada correctamente.';
        this.error = '';
      },
      error: () => {
        this.error = 'No ha sido posible guardar el acta.';
      }
    });
  }

  // Metodo que sirve para rellenar el formulario reactivo con la informacion recibida.
  private rellenarFormulario(actaPartido: ActaPartido): void {
    this.formularioActa.patchValue({
      idPartido: actaPartido.idPartido,
      nombreEquipoLocal: actaPartido.nombreEquipoLocal,
      nombreEquipoVisitante: actaPartido.nombreEquipoVisitante,
      puntosLocal: actaPartido.puntosLocal || 0,
      puntosVisitante: actaPartido.puntosVisitante || 0
    });

    this.estadisticasJugadores.clear();

    actaPartido.estadisticasJugadores.forEach((estadistica) => {
      this.estadisticasJugadores.push(
        this.formBuilder.group({
          idJugador: [estadistica.idJugador, Validators.required],
          nombreJugador: [estadistica.nombreJugador, Validators.required],
          nombreEquipo: [estadistica.nombreEquipo, Validators.required],
          dorsal: [estadistica.dorsal ?? 0],
          puntos: [estadistica.puntos || 0, [Validators.required, Validators.min(0)]],
          asistencias: [estadistica.asistencias || 0, [Validators.required, Validators.min(0)]],
          rebotes: [estadistica.rebotes || 0, [Validators.required, Validators.min(0)]],
          faltas: [estadistica.faltas || 0, [Validators.required, Validators.min(0), Validators.max(5)]],
          minutos: [estadistica.minutos || 0, [Validators.required, Validators.min(0)]]
        })
      );
    });
  }

  // Metodo que sirve para sumar los puntos de un equipo a partir de sus jugadores.
  private sumarPuntosPorEquipo(nombreEquipo: string): number {
    const estadisticasJugadores = this.formularioActa.getRawValue().estadisticasJugadores as Array<Record<string, unknown>>;

    return estadisticasJugadores
      .filter((estadistica) => estadistica['nombreEquipo'] === nombreEquipo)
      .reduce((acumulado, estadistica) => acumulado + Number(estadistica['puntos'] || 0), 0);
  }

  // Metodo que sirve para filtrar y ordenar por dorsal los controles de un equipo.
  private obtenerEstadisticasPorEquipo(nombreEquipo: string): FormGroup[] {
    return this.estadisticasJugadores.controls
      .filter((estadistica) => estadistica.get('nombreEquipo')?.value === nombreEquipo)
      .sort((estadisticaA, estadisticaB) => this.obtenerDorsal(estadisticaA) - this.obtenerDorsal(estadisticaB));
  }

  // Metodo que sirve para obtener el dorsal numerico de un control de estadistica.
  private obtenerDorsal(estadistica: FormGroup): number {
    return Number(estadistica.get('dorsal')?.value ?? 0);
  }
}
