import os
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from typing import Dict
from fastapi import FastAPI, HTTPException, status
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, EmailStr, Field
from dotenv import load_dotenv
import socket
load_dotenv()
app = FastAPI(
    title="Backdigitalvertix API",
    description="API para enviar correos de contacto, migrada de Spring Boot a FastAPI",
    version="0.0.1"
)

# --- CONFIGURACIÓN DE CORS (Equivalente a CorsConfig.java) ---
origins = [
    "https://pre-production.dm46y2d00slmh.amplifyapp.com",
    "https://digital-veltrix.netlify.app",
    "http://localhost:4200",
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=False,
    allow_methods=["GET", "POST", "PUT", "DELETE", "OPTIONS"],
    allow_headers=["*"],
)

# --- MODELO DE DATOS (Equivalente a EmailDTO.java) ---
class EmailDTO(BaseModel):
    nombreRemitente: str = Field(..., min_length=1, description="Nombre de quien envía")
    correoRemitente: EmailStr = Field(..., description="Correo del remitente")
    asunto: str = Field(..., min_length=1)
    mensaje: str = Field(..., min_length=10, description="El mensaje debe tener al menos 10 caracteres")

# --- LÓGICA DE ENVÍO (Equivalente a EmailService.java) ---
def enviar_email_logic(dto: EmailDTO):
    destino_fijo = "veltrixdigital.co@gmail.com"
    password = os.getenv("EMAIL_PASSWORD")
    
    if not password:
        print("DEBUG: EMAIL_PASSWORD no encontrada")
        raise Exception("Contraseña faltante")

    msg = MIMEMultipart()
    msg['From'] = destino_fijo
    msg['To'] = destino_fijo
    msg['Subject'] = f"Mensaje de Contacto: {dto.asunto}"
    msg.add_header('reply-to', dto.correoRemitente)

    cuerpo_html = f"<h3>Nuevo mensaje</h3><p>De: {dto.nombreRemitente}</p><p>{dto.mensaje}</p>"
    msg.attach(MIMEText(cuerpo_html, 'html'))

    try:
        print("DEBUG: Conectando a Gmail via Puerto 587 (TLS)...")
        # Usamos SMTP estándar con un timeout de 30 segundos
        # No usamos la IP directamente para evitar problemas de certificados SSL
        server = smtplib.SMTP('smtp.gmail.com', 587, timeout=30)
        
        server.set_debuglevel(1) # Esto nos dará MUCHA info en los logs de Render
        
        print("DEBUG: Enviando EHLO...")
        server.ehlo()
        
        print("DEBUG: Iniciando STARTTLS...")
        server.starttls() # Cifra la conexión
        
        print("DEBUG: Iniciando Login...")
        server.login(destino_fijo, password)
        
        print("DEBUG: Enviando mensaje...")
        server.send_message(msg)
        
        server.quit()
        print("DEBUG: ¡CORREO ENVIADO EXITOSAMENTE!")
        
    except Exception as e:
        print(f"DETALLE DEL ERROR EN LOGS: {type(e).__name__}: {str(e)}")
        raise e

# --- ENDPOINT (Equivalente a EmailController.java) ---
@app.post("/email/enviarCorreoContacto")
async def enviar_correo_contacto(email_dto: EmailDTO):
    try:
        enviar_email_logic(email_dto)
        return {
            "message": "Tu mensaje ha sido enviado exitosamente. ¡Gracias por contactarnos!",
            "success": True
        }
    except Exception:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail={
                "message": "Error al enviar tu mensaje, por favor inténtalo de nuevo más tarde.",
                "success": False
            }
        )

# Redirección opcional para que la raíz "/" abra Swagger automáticamente
@app.get("/", include_in_schema=False)
async def root():
    from fastapi.responses import RedirectResponse
    return RedirectResponse(url="/docs")