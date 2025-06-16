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

  private apiUrl = 'https://digitalvertix-backend-env.eba-h2g6t3em.us-east-1.elasticbeanstalk.com/email/enviarCorreoContacto';

  constructor(private http: HttpClient) { }

  enviarMensajeContacto(datos: EmailDTO): Observable<RespuestaCorreo> {
    return this.http.post<RespuestaCorreo>(this.apiUrl, datos);
  }
}
