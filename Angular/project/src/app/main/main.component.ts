import { AfterViewInit, Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MainService } from './main.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements AfterViewInit, OnInit {

  nombre: string = '';
  correo: string = '';
  asunto: string = '';
  cuerpoMensaje: string = '';
  mensajeEstado: string = '';
  isSuccess: boolean = false;

  activo: number | null = null;
  preguntas: any[] = [];

  constructor(
    private route: ActivatedRoute,
    private mainService: MainService,
    private translate: TranslateService
  ) {}

  ngOnInit() {
    window.scrollTo({ top: 0, behavior: 'auto' });

    this.cargarPreguntas();

    this.translate.onLangChange.subscribe(() => {
      this.cargarPreguntas();
    });
  }

  cargarPreguntas() {
    this.translate.get('FAQ.QUESTIONS').subscribe((data: any[]) => {
      this.preguntas = data;
    });
  }


  ngAfterViewInit() {
    this.route.fragment.subscribe(fragment => {
      if (fragment) {
        const el = document.getElementById(fragment);
        if (el) {
          el.scrollIntoView({ behavior: 'smooth' });
        }
      }
    });
  }


  enviarFormulario() {
    this.mensajeEstado = '';
    this.isSuccess = false;

    const datosContacto = {
      nombreRemitente: this.nombre,
      correoRemitente: this.correo,
      asunto: this.asunto,
      mensaje: this.cuerpoMensaje
    };

    this.mainService.enviarMensajeContacto(datosContacto).subscribe(
      (response: { message: string; success: boolean }) => {
        this.mensajeEstado = response.message;
        this.isSuccess = response.success;
        this.limpiarFormulario();
      },
      (error: any) => {
        this.mensajeEstado = error.error?.message || 'Error desconocido al enviar el mensaje. Por favor, inténtalo de nuevo.';
        this.isSuccess = false;
        console.error('Error al enviar el correo:', error);
      }
    );
  }

  limpiarFormulario() {
    this.nombre = '';
    this.correo = '';
    this.asunto = '';
    this.cuerpoMensaje = '';
  }

  toggle(index: number) {
    this.activo = this.activo === index ? null : index;
  }

  scrollToTop(): void {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  irA(id: string) {
    const elemento = document.getElementById(id);
    if (elemento) {
      elemento.scrollIntoView({ behavior: 'smooth' });
    }
  }

}
