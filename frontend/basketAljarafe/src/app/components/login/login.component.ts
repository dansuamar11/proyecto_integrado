import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
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
  private readonly cdr = inject(ChangeDetectorRef);

  protected readonly formularioLogin = this.formBuilder.nonNullable.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  protected enviando = false;
  protected error = '';

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
          this.error = 'Las credenciales no son válidas.';
          this.cdr.detectChanges();
          return;
        }

        this.sessionService.establecerSesionDesdeRespuesta(respuestaSesion);
        this.router.navigate(['/panel']);
      },
      error: () => {
        this.error = 'Credenciales no válidas.';
        this.enviando = false;
        this.cdr.detectChanges();
      }
    });
  }
}