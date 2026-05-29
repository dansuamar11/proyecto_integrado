import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Output, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';
import { FormularioCambioPassword } from '../../core/models/api.models';
import { PrivateApiService } from '../../core/services/private-api.service';

@Component({
  selector: 'app-password-change-modal',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './password-change-modal.component.html',
  styleUrl: './password-change-modal.component.css'
})
export class PasswordChangeModalComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly privateApiService = inject(PrivateApiService);

  @Output() readonly cerrado = new EventEmitter<void>();
  @Output() readonly passwordCambiada = new EventEmitter<string>();

  protected enviando = false;
  protected error = '';

  protected readonly formularioCambioPassword = this.formBuilder.group({
    passwordActual: ['', Validators.required],
    nuevaPassword: ['', [Validators.required, Validators.minLength(6)]],
    confirmarNuevaPassword: ['', [Validators.required, Validators.minLength(6)]]
  });

  protected tieneError(nombreControl: 'passwordActual' | 'nuevaPassword' | 'confirmarNuevaPassword', error: string): boolean {
    const control = this.formularioCambioPassword.get(nombreControl);
    return Boolean(control?.touched && control.hasError(error));
  }

  protected nuevasPasswordsNoCoinciden(): boolean {
    const { nuevaPassword, confirmarNuevaPassword } = this.formularioCambioPassword.getRawValue();
    return Boolean(
      this.formularioCambioPassword.get('confirmarNuevaPassword')?.touched &&
      nuevaPassword &&
      confirmarNuevaPassword &&
      nuevaPassword !== confirmarNuevaPassword
    );
  }

  protected cerrar(): void {
    if (this.enviando) {
      return;
    }

    this.formularioCambioPassword.reset({
      passwordActual: '',
      nuevaPassword: '',
      confirmarNuevaPassword: ''
    });
    this.error = '';
    this.cerrado.emit();
  }

  protected enviarFormulario(): void {
    if (this.enviando) {
      return;
    }

    if (this.formularioCambioPassword.invalid) {
      this.formularioCambioPassword.markAllAsTouched();
      return;
    }

    const formulario = this.formularioCambioPassword.getRawValue() as FormularioCambioPassword;

    if (formulario.nuevaPassword !== formulario.confirmarNuevaPassword) {
      this.error = 'Las nuevas contraseñas no coinciden.';
      return;
    }

    this.enviando = true;
    this.error = '';

    this.privateApiService.cambiarPassword(formulario).pipe(
      finalize(() => {
        this.enviando = false;
      })
    ).subscribe({
      next: () => {
        this.passwordCambiada.emit('Contraseña actualizada correctamente.');
        this.cerrar();
      },
      error: (respuesta: HttpErrorResponse) => {
        this.error = respuesta.error?.message || 'No ha sido posible actualizar la contraseña.';
      }
    });
  }
}
