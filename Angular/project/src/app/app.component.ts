import {Component, HostListener} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  mostrarScrollTop: boolean = false;
  title = 'Digital Veltrix';
  scrollToTop(): void {
    window.scrollTo({ top: 0, behavior: 'smooth' });

  }
  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.mostrarScrollTop = window.scrollY > 200;
  }
}
