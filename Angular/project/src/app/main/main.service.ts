import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MainService {

  private baseUrl = 'http://localhost:8081/email'; // Asegúrate de que esta URL sea correcta

  constructor(private http: HttpClient) { }

  enviarMensajeContacto(data: { nombreRemitente: string, correoRemitente: string, asunto: string, mensaje: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/enviarCorreoContacto`, data);
  }
}
