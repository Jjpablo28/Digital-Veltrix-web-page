import { AfterViewInit, Component, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  standalone: false,
})
export class NavbarComponent implements AfterViewInit, OnInit {

  currentUsername: string | null = null;
  showLoginAndSignUp = true;
  showLogOut = false;
  activo: number | null = null;
  rol: string | null = null;
  menuOpen = false;
  loginOpen = false;
  createOpen = true;
  showMenu = false;
  renderMenu = false;

  showDropdown = false;

  languages = [
    { code: 'es', label: 'Español', flag: 'https://flagcdn.com/co.svg' },
    { code: 'en', label: 'English', flag: 'https://flagcdn.com/us.svg' }
  ];

  selectedLanguage = this.languages[0]; // Español por defecto

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
    document.addEventListener('click', () => {
      this.showDropdown = false;
    });
  }

  ngAfterViewInit(): void {}

  scrollToId(id: string) {
    const el = document.getElementById(id);
    if (el) {
      el.scrollIntoView({ behavior: 'smooth' });
    }
  }

  toggleDropdown(event: Event) {
    event.stopPropagation(); // evita que se cierre inmediatamente
    this.showDropdown = !this.showDropdown;
  }

  changeLanguage(lang: any, event: MouseEvent): void {
    event.stopPropagation();
    this.selectedLanguage = lang;
    this.translate.use(lang.code);
    this.showDropdown = false;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    const clickedInside = target.closest('.language-selector');
    if (!clickedInside) {
      this.showDropdown = false;
    }
  }
}
