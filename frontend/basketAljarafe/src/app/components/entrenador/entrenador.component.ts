import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { finalize, forkJoin } from 'rxjs';
import { PasswordChangeModalComponent } from '../password-change-modal/password-change-modal.component';
import { Equipo, Jugador } from '../../core/models/api.models';
import { DatosEntrenador, EstadoResolucionPrivada } from '../../core/resolvers/private-page.resolver';
import { PrivateApiService } from '../../core/services/private-api.service';

@Component({
  selector: 'app-entrenador',
  imports: [CommonModule, ReactiveFormsModule, PasswordChangeModalComponent],
  templateUrl: './entrenador.component.html',
  styleUrl: './entrenador.component.css'
})
export class EntrenadorComponent {
  private readonly tamanoPaginaJugadores = 12;
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly formBuilder = inject(FormBuilder);
  private readonly privateApiService = inject(PrivateApiService);
  private readonly datosPagina =
    this.activatedRoute.snapshot.data['datosPagina'] as EstadoResolucionPrivada<DatosEntrenador>;

  // Variable que sirve para almacenar el equipo del entrenador.
  protected equipo: Equipo | null = this.datosPagina.datos?.equipo ?? null;

  // Variable que sirve para almacenar los jugadores del equipo.
  protected jugadores: Jugador[] = this.ordenarJugadores(this.datosPagina.datos?.jugadores ?? []);

  // Variable que sirve para controlar la carga de la pagina.
  protected cargando = false;

  // Variable que sirve para controlar el envio del alta de jugador.
  protected creandoJugador = false;

  // Variable que sirve para almacenar el mensaje de exito.
  protected mensajeExito = '';

  // Variable que sirve para almacenar el mensaje de error.
  protected error = this.datosPagina.error;

  // Variable que sirve para controlar la visibilidad del modal de cambio de contrasena.
  protected mostrandoModalPassword = false;

  // Variable que sirve para controlar la pagina visible de la plantilla.
  protected paginaJugadores = 0;

  // Variable que sirve para ignorar respuestas antiguas si un jugador se cambia varias veces rapido.
  private readonly versionesEstadoJugadores = new Map<number, number>();

  // Variable que sirve para representar el formulario de alta de jugador.
  protected readonly formularioJugador = this.formBuilder.group({
    nombre: ['', Validators.required],
    apellidos: ['', Validators.required],
    dorsal: [null as number | null, [Validators.required, Validators.min(0), Validators.max(99)]]
  });

  // Metodo que sirve para crear un nuevo jugador.
  protected crearJugador(): void {
    if (this.creandoJugador) {
      return;
    }

    if (this.formularioJugador.invalid) {
      this.formularioJugador.markAllAsTouched();
      return;
    }

    const formularioJugador = this.formularioJugador.getRawValue();

    this.creandoJugador = true;
    this.mensajeExito = '';
    this.error = '';

    this.privateApiService.crearJugador({
      nombre: formularioJugador.nombre || '',
      apellidos: formularioJugador.apellidos || '',
      dorsal: formularioJugador.dorsal ?? 0
    }).pipe(
      finalize(() => {
        this.creandoJugador = false;
      })
    ).subscribe({
      next: (jugadorCreado) => {
        this.mensajeExito = 'Jugador creado correctamente.';
        this.formularioJugador.reset({
          nombre: '',
          apellidos: '',
          dorsal: null
        });
        this.jugadores = this.ordenarJugadores([...this.jugadores, jugadorCreado]);
        this.paginaJugadores = this.obtenerPaginaDeJugador(jugadorCreado.idJugador);
      },
      error: () => {
        this.error = 'No ha sido posible crear el jugador.';
      }
    });
  }

  // Metodo que sirve para dar de baja a un jugador activo.
  protected darBajaJugador(idJugador: number): void {
    this.cambiarEstadoJugador(idJugador, false);
  }

  // Metodo que sirve para dar de alta a un jugador inactivo.
  protected darAltaJugador(idJugador: number): void {
    this.cambiarEstadoJugador(idJugador, true);
  }

  // Metodo que sirve para obtener los jugadores visibles de la pagina actual.
  protected obtenerJugadoresPaginados(): Jugador[] {
    const inicio = this.paginaJugadores * this.tamanoPaginaJugadores;
    return this.jugadores.slice(inicio, inicio + this.tamanoPaginaJugadores);
  }

  // Metodo que sirve para avanzar o retroceder la pagina de plantilla.
  protected cambiarPaginaJugadores(direccion: number): void {
    const nuevaPagina = this.paginaJugadores + direccion;

    if (nuevaPagina >= 0 && nuevaPagina < this.obtenerTotalPaginasJugadores()) {
      this.paginaJugadores = nuevaPagina;
    }
  }

  // Metodo que sirve para comprobar si existe una pagina anterior de plantilla.
  protected puedeRetrocederJugadores(): boolean {
    return this.paginaJugadores > 0;
  }

  // Metodo que sirve para comprobar si existe una pagina siguiente de plantilla.
  protected puedeAvanzarJugadores(): boolean {
    return this.paginaJugadores < this.obtenerTotalPaginasJugadores() - 1;
  }

  // Metodo que sirve para obtener el texto de resumen de la plantilla.
  protected obtenerResumenJugadores(): string {
    if (this.jugadores.length === 0) {
      return '0 resultados';
    }

    const inicio = this.paginaJugadores * this.tamanoPaginaJugadores + 1;
    const fin = Math.min(inicio + this.tamanoPaginaJugadores - 1, this.jugadores.length);
    return `${inicio}-${fin} de ${this.jugadores.length}`;
  }

  // Metodo que sirve para obtener los dorsales disponibles para crear un jugador.
  protected obtenerDorsalesDisponibles(): number[] {
    const dorsalesOcupados = new Set(this.jugadores.map((jugador) => jugador.dorsal));

    return Array.from({ length: 100 }, (_, indice) => indice)
      .filter((dorsal) => dorsal !== 69 && dorsal !== 88 && !dorsalesOcupados.has(dorsal));
  }

  protected abrirModalPassword(): void {
    this.mostrandoModalPassword = true;
  }

  protected cerrarModalPassword(): void {
    this.mostrandoModalPassword = false;
  }

  protected confirmarCambioPassword(mensaje: string): void {
    this.mensajeExito = mensaje;
    this.error = '';
    this.mostrandoModalPassword = false;
  }

  // Metodo que sirve para recargar el equipo y la plantilla del entrenador.
  private recargarDatos(): void {
    this.cargando = true;

    forkJoin({
      equipo: this.privateApiService.obtenerEquipoEntrenador(),
      jugadores: this.privateApiService.obtenerJugadoresEntrenador()
    }).subscribe({
      next: (respuesta) => {
        this.aplicarDatosPanel(respuesta);
        this.cargando = false;
      },
      error: () => {
        this.error = 'No ha sido posible cargar la informacion del entrenador.';
        this.cargando = false;
      }
    });
  }

  // Metodo que sirve para aplicar al componente los datos ya resueltos del panel.
  private aplicarDatosPanel(datos: DatosEntrenador): void {
    this.equipo = datos.equipo;
    this.jugadores = this.ordenarJugadores(datos.jugadores);
    this.ajustarPaginaJugadores();
  }

  // Metodo que sirve para ordenar la plantilla por dorsal y despues por nombre.
  private ordenarJugadores(jugadores: Jugador[]): Jugador[] {
    return [...jugadores].sort((jugadorA, jugadorB) => {
      const comparacionDorsal = this.obtenerDorsal(jugadorA) - this.obtenerDorsal(jugadorB);
      if (comparacionDorsal !== 0) {
        return comparacionDorsal;
      }

      const comparacionApellidos = this.obtenerTextoOrdenable(jugadorA.apellidos)
        .localeCompare(this.obtenerTextoOrdenable(jugadorB.apellidos));
      return comparacionApellidos || this.obtenerTextoOrdenable(jugadorA.nombre)
        .localeCompare(this.obtenerTextoOrdenable(jugadorB.nombre));
    });
  }

  // Metodo que sirve para actualizar un jugador dentro de la plantilla sin recargar la pagina.
  private actualizarJugadorEnPlantilla(jugadorActualizado: Jugador): void {
    this.jugadores = this.ordenarJugadores(
      this.jugadores.map((jugador) =>
        jugador.idJugador === jugadorActualizado.idJugador ? jugadorActualizado : jugador
      )
    );
    this.ajustarPaginaJugadores();
  }

  // Metodo que sirve para activar o desactivar un jugador de forma optimista.
  private cambiarEstadoJugador(idJugador: number, activo: boolean): void {
    const jugadorActual = this.jugadores.find((jugador) => jugador.idJugador === idJugador);

    if (!jugadorActual || jugadorActual.activo === activo) {
      return;
    }

    const version = this.obtenerSiguienteVersionEstado(idJugador);
    const jugadorAnterior = { ...jugadorActual };
    this.mensajeExito = '';
    this.error = '';
    this.actualizarJugadorEnPlantilla({ ...jugadorActual, activo });

    const peticion = activo
      ? this.privateApiService.darAltaJugador(idJugador)
      : this.privateApiService.darBajaJugador(idJugador);

    peticion.subscribe({
      next: (jugadorActualizado) => {
        if (!this.esVersionEstadoActual(idJugador, version)) {
          return;
        }

        this.mensajeExito = activo ? 'Jugador dado de alta correctamente.' : 'Jugador dado de baja correctamente.';
        this.actualizarJugadorEnPlantilla(jugadorActualizado);
      },
      error: () => {
        if (!this.esVersionEstadoActual(idJugador, version)) {
          return;
        }

        this.actualizarJugadorEnPlantilla(jugadorAnterior);
        this.error = activo ? 'No ha sido posible dar de alta al jugador.' : 'No ha sido posible dar de baja al jugador.';
      }
    });
  }

  // Metodo que sirve para obtener el total de paginas de plantilla.
  private obtenerTotalPaginasJugadores(): number {
    return Math.max(1, Math.ceil(this.jugadores.length / this.tamanoPaginaJugadores));
  }

  // Metodo que sirve para reajustar la pagina visible cuando cambia el total de jugadores.
  private ajustarPaginaJugadores(): void {
    const totalPaginas = this.obtenerTotalPaginasJugadores();

    if (this.paginaJugadores >= totalPaginas) {
      this.paginaJugadores = totalPaginas - 1;
    }
  }

  // Metodo que sirve para obtener la pagina donde esta un jugador concreto.
  private obtenerPaginaDeJugador(idJugador: number): number {
    const indice = this.jugadores.findIndex((jugador) => jugador.idJugador === idJugador);
    return indice >= 0 ? Math.floor(indice / this.tamanoPaginaJugadores) : this.paginaJugadores;
  }

  // Metodo que sirve para obtener el dorsal numerico de un jugador.
  private obtenerDorsal(jugador: Jugador): number {
    return jugador.dorsal ?? Number.MAX_SAFE_INTEGER;
  }

  // Metodo que sirve para registrar una nueva version de cambio de estado de un jugador.
  private obtenerSiguienteVersionEstado(idJugador: number): number {
    const version = (this.versionesEstadoJugadores.get(idJugador) ?? 0) + 1;
    this.versionesEstadoJugadores.set(idJugador, version);
    return version;
  }

  // Metodo que sirve para comprobar si la respuesta pertenece al ultimo cambio solicitado.
  private esVersionEstadoActual(idJugador: number, version: number): boolean {
    return this.versionesEstadoJugadores.get(idJugador) === version;
  }

  // Metodo que sirve para evitar errores de ordenacion si llega algun texto nulo desde la API.
  private obtenerTextoOrdenable(valor: string | null | undefined): string {
    return valor ?? '';
  }
}
