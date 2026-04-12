import os
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from typing import Dict
from fastapi import FastAPI, HTTPException, status
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, EmailStr, Field
from dotenv import load_dotenv
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
    # Recuperamos la contraseña de la variable de entorno
    password = os.getenv("EMAIL_PASSWORD")
    
    if not password:
        raise HTTPException(status_code=500, detail="Configuración de correo incompleta en el servidor")

    # Crear el mensaje HTML
    msg = MIMEMultipart()
    msg['From'] = destino_fijo
    msg['To'] = destino_fijo
    msg['Subject'] = f"Mensaje de Contacto desde Web: {dto.asunto}"
    # Reply-To para poder responder directamente al usuario
    msg.add_header('reply-to', dto.correoRemitente)

    cuerpo_html = f"""
    <h3>Nuevo mensaje de contacto</h3>
    <p><strong>Nombre:</strong> {dto.nombreRemitente}</p>
    <p><strong>Correo del remitente:</strong> {dto.correoRemitente}</p>
    <p><strong>Asunto:</strong> {dto.asunto}</p>
    <hr>
    <p><strong>Mensaje:</strong></p>
    <p>{dto.mensaje}</p>
    """
    msg.attach(MIMEText(cuerpo_html, 'html'))

    try:
        # Configuración del servidor SMTP (Gmail)
        server = smtplib.SMTP_SSL('smtp.gmail.com', 465)
        
        server.login(destino_fijo, password)
        server.send_message(msg)
        server.quit()
    except Exception as e:
        # Esto imprimirá el error real en tu consola de VS Code
        print(f"DEBUG: Error real detectado: {e}") 
        raise HTTPException(
            status_code=500,
            detail={
                "message": f"Error técnico: {str(e)}", # Ahora Swagger te dirá el error real
                "success": False
            }
        )

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