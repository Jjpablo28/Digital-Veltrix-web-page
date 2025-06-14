import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {MainService} from "./main.service";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent implements AfterViewInit,OnInit {


  ngOnInit() {
    window.scrollTo({ top: 0, behavior: 'auto' });
  }

  nombre: string = '';
  correo: string = '';
  asunto: string = '';
  cuerpoMensaje: string = '';

  // Variables para mostrar el estado del envío al usuario
  mensajeEstado: string = '';
  isSuccess: boolean = false;

  constructor(private route: ActivatedRoute, private mainService: MainService)  {
  }

  /**
   * Método que se ejecuta cuando el formulario es enviado.
   * Recopila los datos del formulario y los envía al servicio de contacto.
   */
  enviarFormulario() {
    // Limpia cualquier mensaje de estado anterior
    this.mensajeEstado = '';
    this.isSuccess = false;

    // Crea un objeto con los datos del formulario que coincide con la estructura de tu EmailDTO
    const datosContacto = {
      nombreRemitente: this.nombre,
      correoRemitente: this.correo,
      asunto: this.asunto,
      mensaje: this.cuerpoMensaje
    };

    // Llama al método de tu servicio para enviar el mensaje al backend
    this.mainService.enviarMensajeContacto(datosContacto).subscribe(
      response => {
        // Manejo de la respuesta exitosa
        this.mensajeEstado = response.message; // El backend envía un campo 'message'
        this.isSuccess = response.success;     // El backend envía un campo 'success' (true/false)
        this.limpiarFormulario(); // Limpia el formulario después de un envío exitoso
      },
      error => {
        // Manejo de errores
        this.mensajeEstado = error.error?.message || 'Error desconocido al enviar el mensaje. Por favor, inténtalo de nuevo.';
        this.isSuccess = false; // Indica que fue un error
        console.error('Error al enviar el correo:', error);
      }
    );
  }

  /**
   * Método para limpiar los campos del formulario después de un envío.
   */
  limpiarFormulario() {
    this.nombre = '';
    this.correo = '';
    this.asunto = '';
    this.cuerpoMensaje = '';
  }


  ngAfterViewInit() {
    this.route.fragment.subscribe(fragment => {
      if (fragment) {
        const el = document.getElementById(fragment);
        if (el) {
          el.scrollIntoView({behavior: 'smooth'});
        }
      }
    });
  }


  activo: number | null = null;

  preguntas = [
    {
      pregunta: '¿Es complicado solicitar un sistema personalizado?',
      respuesta: 'No, te guiamos paso a paso para definir lo que necesitas.'
    },
    {
      pregunta: '¿Cuánto tiempo toma desarrollar una solución a medida?',
      respuesta: 'Depende del proyecto, pero siempre te damos una estimación clara desde el inicio.'
    },
    {
      pregunta: '¿Puedo solicitar un sistema aunque no tenga conocimientos técnicos?',
      respuesta: '¡Claro! Nos encargamos de traducir tus ideas en soluciones funcionales.'
    },
    {
      pregunta: '¿Ofrecen asesoría para definir los requerimientos del proyecto?',
      respuesta: 'Sí, te ayudamos a estructurar tus necesidades en un plan técnico.'
    },
    {
      pregunta: '¿Qué tipo de empresas o personas pueden contratar sus servicios?',
      respuesta: 'Trabajamos con emprendedores, pymes y cualquier persona con una idea digital.'
    },
    {
      pregunta: '¿Los sistemas que desarrollan funcionan en dispositivos móviles?',
      respuesta: 'Sí, todos nuestros desarrollos son responsivos y adaptables.'
    },
    {
      pregunta: '¿Puedo solicitar solo el diseño o también el desarrollo completo?',
      respuesta: 'Ofrecemos ambos servicios, juntos o por separado.'
    },
    {
      pregunta: '¿Ofrecen mantenimiento o soporte después de entregar el proyecto?',
      respuesta: 'Sí, brindamos soporte técnico y actualizaciones si lo necesitas.'
    },
    {
      pregunta: '¿Puedo ver ejemplos de proyectos anteriores?',
      respuesta: 'Claro, tenemos una selección de proyectos destacados realizados por nosotros.'
    },
    {
      pregunta: '¿Cómo puedo ponerme en contacto con ustedes?',
      respuesta: 'Desde el formulario de contacto o por correo electrónico.'
    }
  ];

  toggle(index: number) {
    this.activo = this.activo === index ? null : index;
  }
  scrollToTop(): void {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

}


