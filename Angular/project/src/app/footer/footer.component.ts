import { Component, HostListener, OnInit } from '@angular/core'; // Agregamos OnInit
import { TranslateService } from "@ngx-translate/core";
import { Router } from "@angular/router"; // Router es inyectado pero no usado, si no lo usas, puedes quitarlo.

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  // standalone: false, // Esto ya se infiere si está en un NgModule, solo es necesario para componentes standalone explícitamente.
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit { // Implementamos OnInit

  // Elimina la primera declaración 'selectedLanguage = 'es';'
  // selectedLanguage = 'es'; // ¡ELIMINAR ESTA LÍNEA!

  showDropdown = false; // Solo si usas un dropdown personalizado, no un <select> nativo

  languages = [
    { code: 'es', label: 'Español', bandera: 'https://flagcdn.com/co.svg' }, // Usas 'bandera'
    { code: 'en', label: 'English', bandera: 'https://flagcdn.com/us.svg' }  // Usas 'bandera'
  ];


  selectedLanguage: { code: string; label: string; bandera: string; };

  constructor(private translate: TranslateService) {
    this.translate.addLangs(['es', 'en']);
    this.translate.setDefaultLang('es');

    const browserLang: string | undefined = this.translate.getBrowserLang();


    const langToUse: string = (!!browserLang?.match(/es|en/)) ? browserLang as string : 'es';

    this.translate.use(langToUse);


    this.selectedLanguage = this.languages.find(l => l.code === langToUse) || this.languages[0];

    this.translate.onLangChange.subscribe(event => {
      this.selectedLanguage = this.languages.find(l => l.code === event.lang) || this.languages[0];
    });
  }

  ngOnInit(): void {

  }


  scrollToId(id: string) {
    const el = document.getElementById(id);
    if (el) {
      el.scrollIntoView({ behavior: 'smooth' });
    } else {

    }
  }

  // Este método solo es necesario si estás implementando un DROPDOWN DE IDIOMA PERSONALIZADO
  // Si usas un <select> nativo en el HTML, esta lógica de `showDropdown` no aplica.
  toggleDropdown(event: Event) {
    event.stopPropagation();
    this.showDropdown = !this.showDropdown;
  }

  // Este método es para cuando seleccionas un idioma desde un DROPDOWN PERSONALIZADO
  // Si usas un <select> nativo, deberías tener un método diferente que maneje el evento `(change)`
  // del <select>, como `changeLanguageBySelect(event: Event)`.
  changeLanguage(lang: { code: string; label: string; bandera: string; }, event: MouseEvent): void {
    event.stopPropagation();
    // this.selectedLanguage ya se actualiza por la suscripción a `onLangChange`
    this.translate.use(lang.code);
    this.showDropdown = false; // Solo si usas dropdown personalizado
  }

  // Este método es para el <select> nativo en el HTML
  changeLanguageBySelect(event: Event): void {
    const target = event.target as HTMLSelectElement | null;
    if (target) {
      const langCode = target.value;
      this.translate.use(langCode);
      // 'selectedLanguage' se actualizará automáticamente a través de la suscripción 'onLangChange'.
    }
  }


  // Este HostListener es para cerrar DROPDOWNS PERSONALIZADOS cuando se hace clic fuera.
  // Si solo usas un <select> nativo, esta lógica no es estrictamente necesaria para el idioma,
  // pero podría ser útil si tienes otros dropdowns en el footer.
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as HTMLElement | null;
    // Si tienes un selector de idioma personalizado con un botón/div y un dropdown,
    // el 'language-selector' sería la clase del contenedor de ese selector.
    // Si solo es un `<select>`, no necesitas esta lógica para el idioma.
    if (target && !target.closest('.language-selector')) {
      this.showDropdown = false;
    }
  }
}
