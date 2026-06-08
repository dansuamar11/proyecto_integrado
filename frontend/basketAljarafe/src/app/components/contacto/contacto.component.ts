import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { PublicApiService } from '../../core/services/public-api.service';

@Component({
  selector: 'app-contacto',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './contacto.component.html',
  styleUrl: './contacto.component.css'
})
export class ContactoComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly publicApiService = inject(PublicApiService);

  // Variable que sirve para representar el formulario de contacto.
  protected readonly formularioContacto = this.formBuilder.nonNullable.group({
    nombreContacto: [''],
    correoContacto: ['', [ContactoComponent.correoValidoValidator]],
    telefonoContacto: ['', [ContactoComponent.telefonoValidoValidator]],
    mensaje: ['', Validators.required]
  }, {
    validators: [ContactoComponent.contactoMinimoValidator]
  });

  // Variable que sirve para controlar el envio del formulario.
  protected enviando = false;

  // Variable que sirve para controlar si ya se intento enviar el formulario.
  protected envioIntentado = false;

  // Variable que sirve para almacenar el mensaje de exito.
  protected mensajeExito = '';

  // Variable que sirve para almacenar el mensaje de error.
  protected error = '';

  // Metodo que sirve para comprobar si un campo concreto debe mostrarse como invalido.
  protected mostrarErrorCampo(nombreCampo: 'correoContacto' | 'telefonoContacto' | 'mensaje'): boolean {
    const control = this.formularioContacto.get(nombreCampo);

    if (!control) {
      return false;
    }

    return control.invalid && (control.touched || control.dirty || this.envioIntentado);
  }

  // Metodo que sirve para comprobar si el correo debe mostrarse como invalido.
  protected mostrarErrorCorreo(): boolean {
    return this.mostrarErrorCampo('correoContacto');
  }

  // Metodo que sirve para comprobar si el telefono debe mostrarse como invalido.
  protected mostrarErrorTelefono(): boolean {
    return this.mostrarErrorCampo('telefonoContacto');
  }

  // Metodo que sirve para comprobar si el mensaje debe mostrarse como invalido.
  protected mostrarErrorMensaje(): boolean {
    return this.mostrarErrorCampo('mensaje');
  }

  // Metodo que sirve para comprobar si faltan el correo y el telefono a la vez.
  protected mostrarErrorContactoMinimo(): boolean {
    return !!this.formularioContacto.errors?.['contactoMinimo']
      && ((this.formularioContacto.get('correoContacto')?.touched ?? false)
        || (this.formularioContacto.get('telefonoContacto')?.touched ?? false));
  }

  // Metodo que sirve para enviar la solicitud de contacto al backend.
  protected enviarFormulario(): void {
    this.envioIntentado = true;

    if (this.formularioContacto.invalid) {
      this.formularioContacto.markAllAsTouched();
      return;
    }

    this.enviando = true;
    this.mensajeExito = '';
    this.error = '';

    this.publicApiService.enviarContacto(this.formularioContacto.getRawValue()).subscribe({
      next: () => {
        this.mensajeExito = 'Solicitud enviada correctamente.';
        this.enviando = false;
        this.envioIntentado = false;
        this.formularioContacto.reset({
          nombreContacto: '',
          correoContacto: '',
          telefonoContacto: '',
          mensaje: ''
        });
      },
      error: () => {
        this.error = 'No ha sido posible enviar la solicitud.';
        this.enviando = false;
      }
    });
  }

  // Validador que sirve para exigir al menos un correo o un telefono.
  private static contactoMinimoValidator(control: AbstractControl): ValidationErrors | null {
    const correoContacto = control.get('correoContacto')?.value?.trim() ?? '';
    const telefonoContacto = control.get('telefonoContacto')?.value?.trim() ?? '';

    if (correoContacto || telefonoContacto) {
      return null;
    }

    return { contactoMinimo: true };
  }

  // Metodo que sirve para exigir un correo con @ y final .com o .es.
  private static correoValidoValidator(control: AbstractControl): ValidationErrors | null {
    const correoContacto = control.value?.trim() ?? '';

    if (!correoContacto) {
      return null;
    }

    return /^[^\s@]+@[^\s@]+\.(com|es)$/i.test(correoContacto)
      ? null
      : { correoInvalido: true };
  }

  // Metodo que sirve para exigir un telefono de 9 digitos.
  private static telefonoValidoValidator(control: AbstractControl): ValidationErrors | null {
    const telefonoContacto = control.value?.trim() ?? '';

    if (!telefonoContacto) {
      return null;
    }

    return /^\d{9}$/.test(telefonoContacto)
      ? null
      : { telefonoInvalido: true };
  }
}
