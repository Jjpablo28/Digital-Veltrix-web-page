import { Component, HostListener, OnInit } from '@angular/core';
import { Router, NavigationEnd } from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {

  mostrarScrollTop: boolean = false;
  title = 'Digital Veltrix';

  constructor(private router: Router) {}

  ngOnInit() {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.setCanonicalURL();
      }
    });
  }

  scrollToTop(): void {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.mostrarScrollTop = window.scrollY > 200;
  }

  setCanonicalURL() {
    const url = 'https://digital-veltrix.netlify.app' + this.router.url;

    let link: HTMLLinkElement =
      document.querySelector("link[rel='canonical']") ||
      document.createElement('link');

    link.setAttribute('rel', 'canonical');
    link.setAttribute('href', url);

    document.head.appendChild(link);
  }
}
