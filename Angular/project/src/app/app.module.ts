import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {NavbarComponent} from "./navbar/navbar.component";
import {FooterComponent} from "./footer/footer.component";
<<<<<<< Updated upstream:Angular/project/src/app/app.module.ts
import {MainComponent} from "./main/main.component";
=======
import { MainComponent } from './main/main.component';
import {FormsModule} from "@angular/forms";
>>>>>>> Stashed changes:Front-Angular/Front/src/app/app.module.ts

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    MainComponent
  ],
<<<<<<< Updated upstream:Angular/project/src/app/app.module.ts
  imports: [
    BrowserModule,
    AppRoutingModule

  ],
=======
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule
    ],
>>>>>>> Stashed changes:Front-Angular/Front/src/app/app.module.ts
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
