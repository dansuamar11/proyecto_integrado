import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PrivateApiService } from '../../core/services/private-api.service';
import { SessionService } from '../../core/services/session.service';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly privateApiService = inject(PrivateApiService);
  private readonly sessionService = inject(SessionService);
  private readonly router = inject(Router);

  // Variable que sirve para representar el formulario de inicio de sesion.
  protected readonly formularioLogin = this.formBuilder.nonNullable.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  // Variable que sirve para controlar el estado del envio.
  protected enviando = false;

  // Variable que sirve para almacenar el mensaje de error.
  protected error = '';

  // Metodo que sirve para iniciar sesion contra Spring Security.
  protected iniciarSesion(): void {
    if (this.formularioLogin.invalid) {
      this.formularioLogin.markAllAsTouched();
      return;
    }

    this.enviando = true;
    this.error = '';

    this.privateApiService.iniciarSesion(this.formularioLogin.getRawValue()).subscribe({
      next: (respuestaSesion) => {
        this.enviando = false;

        if (!respuestaSesion.authenticated) {
          this.error = 'Las credenciales no son validas.';
          return;
        }

        this.sessionService.establecerSesionDesdeRespuesta(respuestaSesion);
        this.router.navigate(['/panel']);
      },
      error: () => {
        this.error = 'Credenciales no validas o servicio no disponible.';
        this.enviando = false;
      }
    });
  }
}
