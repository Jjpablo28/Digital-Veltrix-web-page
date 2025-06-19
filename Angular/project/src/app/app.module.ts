import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NavbarComponent} from "./navbar/navbar.component";
import {FooterComponent} from "./footer/footer.component";
import {MainComponent} from "./main/main.component";
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {TerminosCondicionesComponent} from "./terminos-condiciones/terminos-condiciones.component";
import {PoliticaPrivacidadComponent} from "./politica-privacidad/politica-privacidad.component";
import {CookiesComponent} from "./cookies/cookies.component";

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    MainComponent,
    TerminosCondicionesComponent,
    PoliticaPrivacidadComponent,
    CookiesComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
