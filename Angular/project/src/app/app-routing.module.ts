import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainComponent} from "./main/main.component";
import {TerminosCondicionesComponent} from "./terminos-condiciones/terminos-condiciones.component";
import {PoliticaPrivacidadComponent} from "./politica-privacidad/politica-privacidad.component";
import {CookiesComponent} from "./cookies/cookies.component";

const routes: Routes = [
  {path: '', component: MainComponent},
  {path:'tyc', component:TerminosCondicionesComponent},
  {path:'politica-privacidad', component:PoliticaPrivacidadComponent},
  {path:'cookies', component:CookiesComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    scrollPositionRestoration: 'enabled'
  })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
