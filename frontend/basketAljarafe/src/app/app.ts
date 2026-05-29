import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { NavigationCancel, NavigationEnd, NavigationError, NavigationStart, Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { SessionService } from './core/services/session.service';

@Component({
  selector: 'app-root',
  imports: [CommonModule, RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  private readonly router = inject(Router);
  private readonly sessionService = inject(SessionService);
  private temporizadorCarga: ReturnType<typeof setTimeout> | null = null;

  // Variable que sirve para definir los enlaces publicos del menu principal.
  protected readonly enlacesPublicos = [
    { etiqueta: 'Inicio', ruta: '/inicio' },
    { etiqueta: 'Clasificación', ruta: '/clasificacion' },
    { etiqueta: 'Calendario', ruta: '/calendario' },
    { etiqueta: 'Estadísticas', ruta: '/estadisticas' },
    { etiqueta: 'Contacto', ruta: '/contacto' }
  ];

  // Variable que sirve para exponer el estado de sesion al componente raiz.
  protected readonly sesion = this.sessionService.sesion;

  // Variable que sirve para controlar la capa global de carga durante los cambios de ruta.
  protected readonly navegacionCargando = signal(false);

  // Variable que sirve para indicar si existe un panel privado disponible para la sesion actual.
  protected readonly tienePanelPrivado = computed(() => this.obtenerConfiguracionPanel() !== null);

  // Variable que sirve para mostrar la etiqueta del panel correspondiente al rol autenticado.
  protected readonly etiquetaPanel = computed(() => this.obtenerConfiguracionPanel()?.etiqueta ?? 'Panel');

  // Variable que sirve para obtener la ruta del panel correspondiente al rol autenticado.
  protected readonly rutaPanel = computed(() => this.obtenerConfiguracionPanel()?.ruta ?? '/panel');

  // Metodo que sirve para cargar la sesion actual al iniciar la aplicacion.
  ngOnInit(): void {
    this.sessionService.inicializarSesion();

    this.router.events.subscribe((evento) => {
      if (evento instanceof NavigationStart) {
        this.programarCargaGlobal();
      }

      if (
        evento instanceof NavigationEnd ||
        evento instanceof NavigationCancel ||
        evento instanceof NavigationError
      ) {
        this.limpiarCargaGlobal();
      }
    });
  }

  // Metodo que sirve para cerrar la sesion actual y volver a la pagina de inicio.
  protected cerrarSesion(): void {
    this.sessionService.cerrarSesion().subscribe(() => {
      this.router.navigate(['/inicio']);
    });
  }

  // Metodo que sirve para devolver la configuracion del panel segun el rol principal de la sesion.
  private obtenerConfiguracionPanel(): { etiqueta: string; ruta: string } | null {
    const roles = this.sesion().roles;

    if (roles.includes('ROLE_ADMIN')) {
      return {
        etiqueta: 'Panel Admin',
        ruta: '/gerente/administrar'
      };
    }

    if (roles.includes('ROLE_GERENTE')) {
      return {
        etiqueta: 'Panel Gerente',
        ruta: '/gerente/administrar'
      };
    }

    if (roles.includes('ROLE_ENTRENADOR')) {
      return {
        etiqueta: 'Panel Entrenador',
        ruta: '/entrenador/administrar-equipo'
      };
    }

    if (roles.includes('ROLE_ARBITRO')) {
      return {
        etiqueta: 'Panel Arbitro',
        ruta: '/arbitro/ficha-partidos'
      };
    }

    return null;
  }

  // Metodo que sirve para retrasar la carga global y evitar parpadeos cuando la navegacion es instantanea.
  private programarCargaGlobal(): void {
    this.limpiarTemporizadorCarga();

    this.temporizadorCarga = setTimeout(() => {
      this.navegacionCargando.set(true);
      this.temporizadorCarga = null;
    }, 180);
  }

  // Metodo que sirve para ocultar la carga global cuando termina la navegacion.
  private limpiarCargaGlobal(): void {
    this.limpiarTemporizadorCarga();
    this.navegacionCargando.set(false);
  }

  // Metodo que sirve para cancelar el temporizador pendiente de la carga global.
  private limpiarTemporizadorCarga(): void {
    if (this.temporizadorCarga) {
      clearTimeout(this.temporizadorCarga);
      this.temporizadorCarga = null;
    }
  }
}
