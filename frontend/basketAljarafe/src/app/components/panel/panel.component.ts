import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from '../../core/services/session.service';

@Component({
  selector: 'app-panel',
  imports: [CommonModule],
  templateUrl: './panel.component.html',
  styleUrl: './panel.component.css'
})
export class PanelComponent implements OnInit {
  private readonly sessionService = inject(SessionService);
  private readonly router = inject(Router);

  // Variable que sirve para informar del estado temporal de redireccion.
  protected mensaje = 'Redirigiendo al panel correspondiente...';

  // Metodo que sirve para enviar al usuario a la pantalla asociada a su rol.
  ngOnInit(): void {
    this.sessionService.cargarSesion().subscribe((sesion) => {
      if (!sesion.autenticado) {
        this.router.navigate(['/login']);
        return;
      }

      if (sesion.roles.includes('ROLE_ADMIN') || sesion.roles.includes('ROLE_GERENTE')) {
        this.router.navigate(['/gerente/administrar']);
        return;
      }

      if (sesion.roles.includes('ROLE_ENTRENADOR')) {
        this.router.navigate(['/entrenador/administrar-equipo']);
        return;
      }

      if (sesion.roles.includes('ROLE_ARBITRO')) {
        this.router.navigate(['/arbitro/ficha-partidos']);
        return;
      }

      this.router.navigate(['/inicio']);
    });
  }
}
