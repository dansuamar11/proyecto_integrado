import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, NgZone, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { PasswordChangeModalComponent } from '../password-change-modal/password-change-modal.component';
import { Equipo, FormularioGenerarPartido, Partido, Usuario, convertirFechaApi } from '../../core/models/api.models';
import { DatosGerente, EstadoResolucionPrivada } from '../../core/resolvers/private-page.resolver';
import { PrivateApiService } from '../../core/services/private-api.service';
import { SessionService } from '../../core/services/session.service';

@Component({
  selector: 'app-gerente',
  imports: [CommonModule, ReactiveFormsModule, RouterLink, PasswordChangeModalComponent],
  templateUrl: './gerente.component.html',
  styleUrl: './gerente.component.css'
})
export class GerenteComponent {
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly formBuilder = inject(FormBuilder);
  private readonly privateApiService = inject(PrivateApiService);
  private readonly sessionService = inject(SessionService);
  private readonly ngZone = inject(NgZone);
  private readonly changeDetectorRef = inject(ChangeDetectorRef);
  private readonly datosPagina = this.activatedRoute.snapshot.data['datosPagina'] as EstadoResolucionPrivada<DatosGerente>;

  // Variable que sirve para almacenar los usuarios registrados.
  protected usuarios: Usuario[] = this.datosPagina.datos?.usuarios ?? [];

  // Variable que sirve para almacenar los equipos registrados.
  protected equipos: Equipo[] = this.datosPagina.datos?.equipos ?? [];

  // Variable que sirve para almacenar los entrenadores disponibles.
  protected entrenadores: Usuario[] = this.datosPagina.datos?.entrenadores ?? [];

  // Variable que sirve para almacenar los arbitros disponibles.
  protected arbitros: Usuario[] = this.datosPagina.datos?.arbitros ?? [];

  // Variable que sirve para almacenar los partidos pendientes sin arbitro asignado.
  protected partidosSinArbitro: Partido[] = this.datosPagina.datos?.partidosSinArbitro ?? [];

  // Variable que sirve para almacenar todos los partidos pendientes para asignar o cambiar arbitro.
  protected partidosPendientes: Partido[] = this.datosPagina.datos?.partidosPendientes ?? [];

  // Variable que sirve para almacenar el total de solicitudes de contacto pendientes.
  protected totalSolicitudesContacto = this.datosPagina.datos?.solicitudesContacto.length ?? 0;

  // Variable que sirve para controlar el estado de carga de la pagina.
  protected cargando = false;

  // Variable que sirve para almacenar el mensaje de exito.
  protected mensajeExito = '';

  // Variable que sirve para almacenar errores de acciones puntuales del panel.
  protected error = '';

  // Variable que sirve para almacenar el error de carga inicial del panel.
  protected readonly errorCarga = this.datosPagina.error;

  // Variable que sirve para controlar la visibilidad del modal de cambio de contrasena.
  protected mostrandoModalPassword = false;

  // Variable que sirve para controlar la visibilidad del modal de generar partido.
  protected mostrandoModalPartido = false;

  // Variable que sirve para mostrar errores del modal de partidos por encima del propio modal.
  protected modalPartidoError = '';

  // Variable que sirve para obtener la fecha minima permitida en el selector de fecha.
  protected get fechaMinimaFormateada(): string {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  // Variable que sirve para exponer las franjas horarias permitidas al crear partidos.
  protected readonly horasPartidoDisponibles = ['12:00', '13:00', '16:00', '17:00', '18:00'];

  // Variable que sirve para exponer los equipos disponibles en el selector de equipo visitante (excluyendo el local).
  protected get equiposDisponiblesModal(): Equipo[] {
    const localId = this.formularioPartido.get('idEquipoLocal')?.value;
    if (localId) {
      return this.equipos.filter((e) => e.idEquipo !== localId);
    }
    return this.equipos;
  }

  // Variable que sirve para almacenar el filtro actual de rol del listado de usuarios.
  protected filtroRolUsuarios = '';

  // Variable que sirve para controlar la pagina visible del listado de usuarios.
  protected paginaUsuarios = 0;

  // Variable que sirve para controlar la pagina visible del listado de equipos.
  protected paginaEquipos = 0;

  // Variable que sirve para ignorar respuestas antiguas si un usuario cambia varias veces rapido.
  private readonly versionesEstadoUsuarios = new Map<number, number>();

  // Variable que sirve para representar el formulario de alta de usuario.
  protected readonly formularioUsuario = this.formBuilder.nonNullable.group({
    nombreUsuario: ['', Validators.required],
    username: ['', Validators.required],
    password: ['', Validators.required],
    nombreRol: ['ARBITRO', Validators.required]
  });

  // Variable que sirve para representar el formulario de alta de equipo.
  protected readonly formularioEquipo = this.formBuilder.group({
    nombre: ['', Validators.required],
    idEntrenador: [null as number | null, Validators.required]
  });

  // Variable que sirve para representar el formulario de cambio de entrenador.
  protected readonly formularioCambioEntrenador = this.formBuilder.group({
    idEquipo: [null as number | null, Validators.required],
    idEntrenador: [null as number | null, Validators.required]
  });

  // Variable que sirve para representar el formulario de asignacion de arbitro a partido.
  protected readonly formularioAsignacionArbitro = this.formBuilder.group({
    idArbitro: [null as number | null, Validators.required],
    idPartido: [null as number | null, Validators.required]
  });

  // Variable que sirve para representar el formulario de generacion de un partido.
  protected readonly formularioPartido = this.formBuilder.group({
    idEquipoLocal: [null as number | null, Validators.required],
    idEquipoVisitante: [null as number | null, Validators.required],
    idArbitro: [null as number | null, Validators.required],
    fecha: ['', Validators.required],
    hora: ['12:00', Validators.required]
  });

  // Metodo que sirve para comprobar si la sesion actual pertenece a un administrador.
  protected esAdmin(): boolean {
    return this.sessionService.tieneAlgunRol(['ROLE_ADMIN']);
  }

  // Metodo que sirve para obtener la etiqueta principal de gestion segun el rol actual.
  protected obtenerEtiquetaGestion(): string {
    return this.esAdmin() ? 'usuarios' : 'árbitros/entrenadores';
  }

  // Metodo que sirve para obtener la etiqueta singular de alta segun el rol actual.
  protected obtenerEtiquetaAlta(): string {
    return this.esAdmin() ? 'usuario' : 'árbitro/entrenador';
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
    this.cerrarModalPassword();
  }

  // Metodo que sirve para abrir el modal de generacion de partido.
  protected abrirModalGenerarPartido(): void {
    this.mostrandoModalPartido = true;
    this.formularioPartido.reset({
      idEquipoLocal: null,
      idEquipoVisitante: null,
      idArbitro: null,
      fecha: '',
      hora: '12:00'
    });
    this.error = '';
    this.modalPartidoError = '';
  }

  // Metodo que sirve para cerrar el modal de generacion de partido.
  protected cerrarModalPartido(): void {
    this.mostrandoModalPartido = false;
    this.modalPartidoError = '';
  }

  // Metodo que sirve para cerrar manualmente el aviso flotante del modal.
  protected cerrarErrorModalPartido(): void {
    this.modalPartidoError = '';
  }

  // Metodo que sirve para crear un nuevo partido desde el modal.
  protected generarPartido(): void {
    if (this.cargando) {
      return;
    }

    if (this.formularioPartido.invalid) {
      this.formularioPartido.markAllAsTouched();
      return;
    }

    this.cargando = true;
    this.mensajeExito = '';
    this.error = '';
    this.modalPartidoError = '';

    const formulario = this.formularioPartido.getRawValue();

    this.privateApiService.generarPartido({
      idEquipoLocal: formulario.idEquipoLocal,
      idEquipoVisitante: formulario.idEquipoVisitante,
      idArbitro: formulario.idArbitro,
      fecha: `${formulario.fecha}T${formulario.hora}:00`
    } as FormularioGenerarPartido).subscribe({
      next: (partidoCreado) => {
        this.actualizarVista(() => {
          this.mensajeExito = 'Partido generado correctamente.';
          if (!partidoCreado.arbitro) {
            this.partidosSinArbitro = [...this.partidosSinArbitro, partidoCreado]
              .sort((partidoA, partidoB) =>
                convertirFechaApi(partidoA.fecha)!.getTime() - convertirFechaApi(partidoB.fecha)!.getTime()
              );
          }
          this.partidosPendientes = [...this.partidosPendientes, partidoCreado]
            .sort((partidoA, partidoB) =>
              convertirFechaApi(partidoA.fecha)!.getTime() - convertirFechaApi(partidoB.fecha)!.getTime()
            );
          this.cerrarModalPartido();
          this.cargando = false;
        });
      },
      error: (respuesta: HttpErrorResponse) => {
        this.actualizarVista(() => {
          this.modalPartidoError = respuesta.error?.message || 'No ha sido posible crear el partido.';
          this.cargando = false;
        });
      }
    });
  }

  // Metodo que sirve para desactivar un usuario del listado administrativo.
  protected desactivarUsuario(idUsuario: number): void {
    this.cambiarEstadoUsuario(idUsuario, false);
  }

  // Metodo que sirve para reactivar un usuario del listado administrativo.
  protected activarUsuario(idUsuario: number): void {
    this.cambiarEstadoUsuario(idUsuario, true);
  }

  // Metodo que sirve para actualizar el filtro de rol del listado de usuarios.
  protected actualizarFiltroRolUsuarios(evento: Event): void {
    const selector = evento.target as HTMLSelectElement;
    this.filtroRolUsuarios = selector.value;
    this.paginaUsuarios = 0;
  }

  // Metodo que sirve para obtener los roles disponibles del filtro de usuarios.
  protected obtenerRolesFiltroUsuarios(): string[] {
    return Array.from(new Set(this.obtenerUsuariosGestionables().map((usuario) => usuario.rol.nombreRol))).sort((rolA, rolB) => rolA.localeCompare(rolB));
  }

  // Metodo que sirve para obtener los usuarios visibles de la pagina actual.
  protected obtenerUsuariosPaginados(): Usuario[] {
    const usuariosFiltrados = this.obtenerUsuariosFiltrados();
    const inicio = this.paginaUsuarios * 5;
    return usuariosFiltrados.slice(inicio, inicio + 5);
  }

  // Metodo que sirve para avanzar o retroceder la pagina del listado de usuarios.
  protected cambiarPaginaUsuarios(direccion: number): void {
    const nuevaPagina = this.paginaUsuarios + direccion;

    if (nuevaPagina >= 0 && nuevaPagina < this.obtenerTotalPaginasUsuarios()) {
      this.paginaUsuarios = nuevaPagina;
    }
  }

  // Metodo que sirve para comprobar si existe una pagina anterior de usuarios.
  protected puedeRetrocederUsuarios(): boolean {
    return this.paginaUsuarios > 0;
  }

  // Metodo que sirve para comprobar si existe una pagina siguiente de usuarios.
  protected puedeAvanzarUsuarios(): boolean {
    return this.paginaUsuarios < this.obtenerTotalPaginasUsuarios() - 1;
  }

  // Metodo que sirve para obtener el texto de resumen del listado de usuarios.
  protected obtenerResumenUsuarios(): string {
    const totalUsuarios = this.obtenerUsuariosFiltrados().length;

    if (totalUsuarios === 0) {
      return '0 resultados';
    }

    const inicio = this.paginaUsuarios * 5 + 1;
    const fin = Math.min(inicio + 4, totalUsuarios);
    return `${inicio}-${fin} de ${totalUsuarios}`;
  }

  // Metodo que sirve para comprobar si un usuario puede activarse o desactivarse desde la vista actual.
  protected puedeCambiarEstadoUsuario(usuario: Usuario): boolean {
    if (usuario.username === this.sessionService.sesion().username) {
      return false;
    }

    return this.esAdmin() || usuario.rol.nombreRol === 'ARBITRO' || usuario.rol.nombreRol === 'ENTRENADOR';
  }

  // Metodo que sirve para obtener los equipos visibles de la pagina actual.
  protected obtenerEquiposPaginados(): Equipo[] {
    const inicio = this.paginaEquipos * 6;
    return this.equipos.slice(inicio, inicio + 6);
  }

  // Metodo que sirve para avanzar o retroceder la pagina del listado de equipos.
  protected cambiarPaginaEquipos(direccion: number): void {
    const nuevaPagina = this.paginaEquipos + direccion;

    if (nuevaPagina >= 0 && nuevaPagina < this.obtenerTotalPaginasEquipos()) {
      this.paginaEquipos = nuevaPagina;
    }
  }

  // Metodo que sirve para comprobar si existe una pagina anterior de equipos.
  protected puedeRetrocederEquipos(): boolean {
    return this.paginaEquipos > 0;
  }

  // Metodo que sirve para comprobar si existe una pagina siguiente de equipos.
  protected puedeAvanzarEquipos(): boolean {
    return this.paginaEquipos < this.obtenerTotalPaginasEquipos() - 1;
  }

  // Metodo que sirve para obtener el texto de resumen del listado de equipos.
  protected obtenerResumenEquipos(): string {
    if (this.equipos.length === 0) {
      return '0 resultados';
    }

    const inicio = this.paginaEquipos * 6 + 1;
    const fin = Math.min(inicio + 5, this.equipos.length);
    return `${inicio}-${fin} de ${this.equipos.length}`;
  }

  // Metodo que sirve para construir la etiqueta visible de un partido sin arbitro asignado.
  protected obtenerEtiquetaPartidoSinArbitro(partido: Partido): string {
    const fecha = convertirFechaApi(partido.fecha);
    const fechaFormateada = fecha
      ? `${String(fecha.getDate()).padStart(2, '0')}/${String(fecha.getMonth() + 1).padStart(2, '0')}/${fecha.getFullYear()} ${String(fecha.getHours()).padStart(2, '0')}:${String(fecha.getMinutes()).padStart(2, '0')}`
      : 'Fecha no disponible';

    return `${partido.equipoLocal.nombre} vs ${partido.equipoVisitante.nombre} - ${fechaFormateada}`;
  }

  // Metodo que sirve para crear un usuario nuevo.
  protected crearUsuario(): void {
    if (this.cargando) {
      return;
    }

    if (this.formularioUsuario.invalid) {
      this.formularioUsuario.markAllAsTouched();
      return;
    }

    this.cargando = true;
    this.mensajeExito = '';
    this.error = '';

    this.privateApiService.crearUsuario(this.formularioUsuario.getRawValue()).subscribe({
      next: (usuarioCreado) => {
        this.actualizarVista(() => {
          this.mensajeExito = 'Usuario creado correctamente.';
          this.formularioUsuario.reset({
            nombreUsuario: '',
            username: '',
            password: '',
            nombreRol: 'Árbitro'
          });
          this.usuarios = this.ordenarUsuarios([...this.usuarios, usuarioCreado]);

          if (usuarioCreado.rol.nombreRol === 'ENTRENADOR' && usuarioCreado.activo) {
            this.entrenadores = this.ordenarUsuarios([...this.entrenadores, usuarioCreado]);
          }

          this.paginaUsuarios = this.obtenerPaginaDeUsuario(usuarioCreado.idUsuario);
          this.cargando = false;
        });
      },
      error: (respuesta: HttpErrorResponse) => {
        this.actualizarVista(() => {
          this.error = respuesta.error?.message || 'No ha sido posible crear el usuario.';
          this.cargando = false;
        });
      }
    });
  }

  // Metodo que sirve para crear un equipo nuevo.
  protected crearEquipo(): void {
    if (this.formularioEquipo.invalid) {
      this.formularioEquipo.markAllAsTouched();
      return;
    }

    const formularioEquipo = this.formularioEquipo.getRawValue();

    this.privateApiService.crearEquipo({
      nombre: formularioEquipo.nombre || '',
      idEntrenador: formularioEquipo.idEntrenador
    }).subscribe({
      next: () => {
        this.mensajeExito = 'Equipo creado correctamente.';
        this.error = '';
        this.formularioEquipo.reset({
          nombre: '',
          idEntrenador: null
        });
        this.recargarDatos();
      },
      error: () => {
        this.error = 'No ha sido posible crear el equipo.';
      }
    });
  }

  // Metodo que sirve para cambiar el entrenador de un equipo.
  protected cambiarEntrenador(): void {
    if (this.formularioCambioEntrenador.invalid) {
      this.formularioCambioEntrenador.markAllAsTouched();
      return;
    }

    this.privateApiService.cambiarEntrenador(this.formularioCambioEntrenador.getRawValue()).subscribe({
      next: (equipoActualizado) => {
        this.mensajeExito = 'Entrenador actualizado correctamente.';
        this.error = '';
        this.actualizarEquipoTrasCambioEntrenador(equipoActualizado);
        this.sincronizarEntrenadoresDisponibles();
        this.formularioCambioEntrenador.reset({
          idEquipo: null,
          idEntrenador: null
        });
      },
      error: () => {
        this.error = 'No ha sido posible actualizar el entrenador.';
      }
    });
  }

  // Metodo que sirve para asignar o cambiar un arbitro en un partido pendiente.
  protected asignarArbitroAPartido(): void {
    if (this.formularioAsignacionArbitro.invalid) {
      this.formularioAsignacionArbitro.markAllAsTouched();
      return;
    }

    this.privateApiService.asignarArbitroAPartido(this.formularioAsignacionArbitro.getRawValue()).subscribe({
      next: (partidoActualizado) => {
        this.mensajeExito = 'Arbitro actualizado correctamente.';
        this.error = '';
        this.partidosSinArbitro = this.partidosSinArbitro.filter((partido) => partido.idPartido !== partidoActualizado.idPartido);
        this.partidosPendientes = this.partidosPendientes.map((partido) =>
          partido.idPartido === partidoActualizado.idPartido ? partidoActualizado : partido
        );
        this.formularioAsignacionArbitro.reset({
          idArbitro: null,
          idPartido: null
        });
      },
      error: (respuesta: HttpErrorResponse) => {
        this.error = respuesta.error?.message || 'No ha sido posible asignar el arbitro.';
      }
    });
  }

  // Metodo que sirve para recargar los listados del panel.
  private recargarDatos(): void {
    this.cargando = true;

    forkJoin({
      usuarios: this.privateApiService.obtenerUsuarios(),
      equipos: this.privateApiService.obtenerEquipos(),
      entrenadores: this.privateApiService.obtenerEntrenadores(),
      arbitros: this.privateApiService.obtenerArbitros(),
      partidosSinArbitro: this.privateApiService.obtenerPartidosSinArbitro(),
      partidosPendientes: this.privateApiService.obtenerPartidosPendientes(),
      solicitudesContacto: this.privateApiService.obtenerSolicitudesContacto()
    }).subscribe({
      next: (respuesta) => {
        this.aplicarDatosPanel(respuesta);
        this.error = '';
        this.cargando = false;
      },
      error: () => {
        this.error = 'No ha sido posible cargar la informacion del gerente.';
        this.cargando = false;
      }
    });
  }

  // Metodo que sirve para aplicar al componente los datos ya resueltos del panel.
  private aplicarDatosPanel(datos: DatosGerente): void {
    this.usuarios = this.ordenarUsuarios(datos.usuarios);
    this.equipos = datos.equipos;
    this.entrenadores = this.ordenarUsuarios(datos.entrenadores);
    this.arbitros = this.ordenarUsuarios(datos.arbitros);
    this.partidosSinArbitro = datos.partidosSinArbitro;
    this.partidosPendientes = datos.partidosPendientes;
    this.totalSolicitudesContacto = datos.solicitudesContacto.length;
    this.ajustarPaginaUsuarios();
    this.ajustarPaginaEquipos();
  }

  // Metodo que sirve para obtener el numero total de paginas del listado de usuarios.
  private obtenerTotalPaginasUsuarios(): number {
    return Math.max(1, Math.ceil(this.obtenerUsuariosFiltrados().length / 5));
  }

  // Metodo que sirve para aplicar el filtro actual sobre el listado de usuarios.
  private obtenerUsuariosFiltrados(): Usuario[] {
    return this.obtenerUsuariosGestionables()
      .filter((usuario) => !this.filtroRolUsuarios || usuario.rol.nombreRol === this.filtroRolUsuarios)
      .sort((usuarioA, usuarioB) => usuarioA.nombreUsuario.localeCompare(usuarioB.nombreUsuario));
  }

  // Metodo que sirve para obtener los usuarios visibles segun el perfil actual.
  private obtenerUsuariosGestionables(): Usuario[] {
    if (this.esAdmin()) {
      return this.usuarios;
    }

    return this.usuarios.filter((usuario) => usuario.rol.nombreRol === 'ARBITRO' || usuario.rol.nombreRol === 'ENTRENADOR');
  }

  // Metodo que sirve para reajustar la pagina visible cuando cambia el total de usuarios.
  private ajustarPaginaUsuarios(): void {
    const totalPaginas = this.obtenerTotalPaginasUsuarios();

    if (this.paginaUsuarios >= totalPaginas) {
      this.paginaUsuarios = totalPaginas - 1;
    }
  }

  // Metodo que sirve para obtener el total de paginas del listado de equipos.
  private obtenerTotalPaginasEquipos(): number {
    return Math.max(1, Math.ceil(this.equipos.length / 6));
  }

  // Metodo que sirve para reajustar la pagina visible cuando cambia el total de equipos.
  private ajustarPaginaEquipos(): void {
    const totalPaginas = this.obtenerTotalPaginasEquipos();

    if (this.paginaEquipos >= totalPaginas) {
      this.paginaEquipos = totalPaginas - 1;
    }
  }

  // Metodo que sirve para actualizar un usuario dentro del listado sin recargar la pagina.
  private actualizarUsuarioEnListado(usuarioActualizado: Usuario): void {
    this.usuarios = this.ordenarUsuarios(
      this.usuarios.map((usuario) =>
        usuario.idUsuario === usuarioActualizado.idUsuario ? usuarioActualizado : usuario
      )
    );
    this.ajustarPaginaUsuarios();
  }

  // Metodo que sirve para reflejar inmediatamente el nuevo entrenador en el listado de equipos.
  private actualizarEquipoTrasCambioEntrenador(equipoActualizado: Equipo): void {
    this.equipos = this.equipos.map((equipo) =>
      equipo.idEquipo === equipoActualizado.idEquipo ? equipoActualizado : equipo
    );
  }

  // Metodo que sirve para activar o desactivar un usuario de forma optimista.
  private cambiarEstadoUsuario(idUsuario: number, activo: boolean): void {
    const usuarioActual = this.usuarios.find((usuario) => usuario.idUsuario === idUsuario);

    if (!usuarioActual || usuarioActual.activo === activo || !this.puedeCambiarEstadoUsuario(usuarioActual)) {
      return;
    }

    const version = this.obtenerSiguienteVersionEstadoUsuario(idUsuario);
    const usuarioAnterior = { ...usuarioActual };
    this.mensajeExito = '';
    this.error = '';
    this.actualizarUsuarioEnListado({ ...usuarioActual, activo });

    const peticion = activo
      ? this.privateApiService.activarUsuario(idUsuario)
      : this.privateApiService.desactivarUsuario(idUsuario);

    peticion.subscribe({
      next: (usuarioActualizado) => {
        if (!this.esVersionEstadoUsuarioActual(idUsuario, version)) {
          return;
        }

        this.mensajeExito = activo ? 'Usuario reactivado correctamente.' : 'Usuario desactivado correctamente.';
        this.actualizarUsuarioEnListado(usuarioActualizado);
        this.sincronizarEntrenadoresDisponibles();
      },
      error: () => {
        if (!this.esVersionEstadoUsuarioActual(idUsuario, version)) {
          return;
        }

        this.actualizarUsuarioEnListado(usuarioAnterior);
        this.sincronizarEntrenadoresDisponibles();
        this.error = activo ? 'No ha sido posible reactivar el usuario.' : 'No ha sido posible desactivar el usuario.';
      }
    });
  }

  // Metodo que sirve para sincronizar los entrenadores disponibles tras activar o desactivar usuarios.
  private sincronizarEntrenadoresDisponibles(): void {
    this.privateApiService.obtenerEntrenadores().subscribe({
      next: (entrenadores) => {
        this.entrenadores = this.ordenarUsuarios(entrenadores);
      }
    });
  }

  // Metodo que sirve para ordenar usuarios alfabeticamente.
  private ordenarUsuarios(usuarios: Usuario[]): Usuario[] {
    return [...usuarios].sort((usuarioA, usuarioB) => usuarioA.nombreUsuario.localeCompare(usuarioB.nombreUsuario));
  }

  // Metodo que sirve para obtener la pagina donde esta un usuario concreto.
  private obtenerPaginaDeUsuario(idUsuario: number): number {
    const indice = this.obtenerUsuariosFiltrados().findIndex((usuario) => usuario.idUsuario === idUsuario);
    return indice >= 0 ? Math.floor(indice / 5) : this.paginaUsuarios;
  }

  // Metodo que sirve para registrar una nueva version de cambio de estado de un usuario.
  private obtenerSiguienteVersionEstadoUsuario(idUsuario: number): number {
    const version = (this.versionesEstadoUsuarios.get(idUsuario) ?? 0) + 1;
    this.versionesEstadoUsuarios.set(idUsuario, version);
    return version;
  }

  // Metodo que sirve para comprobar si la respuesta pertenece al ultimo cambio solicitado.
  private esVersionEstadoUsuarioActual(idUsuario: number, version: number): boolean {
    return this.versionesEstadoUsuarios.get(idUsuario) === version;
  }

  // Metodo que sirve para asegurar que los cambios asincronos actualicen la vista al momento.
  private actualizarVista(accion: () => void): void {
    this.ngZone.run(() => {
      accion();
      this.changeDetectorRef.detectChanges();
    });
  }
}
