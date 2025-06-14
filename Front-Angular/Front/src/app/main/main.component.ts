import { Component } from '@angular/core';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent {


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
}


