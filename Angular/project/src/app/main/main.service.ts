import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

interface EmailDTO {
  nombreRemitente: string;
  correoRemitente: string;
  asunto: string;
  mensaje: string;
}

interface RespuestaCorreo {
  message: string;
  success: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class MainService {

  private apiUrl = 'https://tudominio.com/api/contacto'; // Cambia esto por la URL real de tu backend

  constructor(private http: HttpClient) { }

  enviarMensajeContacto(datos: EmailDTO): Observable<RespuestaCorreo> {
    return this.http.post<RespuestaCorreo>(this.apiUrl, datos);
  }
}
