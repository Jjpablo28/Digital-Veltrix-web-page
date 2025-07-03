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

  private apiUrl = 'https://vf80c99t7i.execute-api.us-east-1.amazonaws.com/DigitalVertix/enviarCorreoContacto';

  constructor(private http: HttpClient) { }

  enviarMensajeContacto(datos: EmailDTO): Observable<RespuestaCorreo> {
    return this.http.post<RespuestaCorreo>(this.apiUrl, datos);
  }
}
