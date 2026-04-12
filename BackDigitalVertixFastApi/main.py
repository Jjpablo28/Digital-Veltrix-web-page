import os
import resend  # Importamos la nueva librería
from fastapi import FastAPI, HTTPException, status, BackgroundTasks
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, EmailStr, Field
from dotenv import load_dotenv

load_dotenv()

# Configuramos la llave de Resend
resend.api_key = os.getenv("RESEND_API_KEY")

app = FastAPI(
    title="Backdigitalvertix API",
    description="API para enviar correos usando Resend",
    version="0.0.1"
)

# --- CONFIGURACIÓN DE CORS ---
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

class EmailDTO(BaseModel):
    nombreRemitente: str = Field(..., min_length=1)
    correoRemitente: EmailStr = Field(...)
    asunto: str = Field(..., min_length=1)
    mensaje: str = Field(..., min_length=10)

# Lógica de envío simplificada
def enviar_email_logic(dto: EmailDTO):
    try:
        # Si no has verificado un dominio en Resend, DEBES usar "onboarding@resend.dev"
        params = {
            "from": "onboarding@resend.dev",
            "to": "veltrixdigital.co@gmail.com",
            "subject": f"Mensaje de Contacto: {dto.asunto}",
            "reply_to": dto.correoRemitente, # Permite que respondas al usuario directamente
            "html": f"""
                <h3>Nuevo mensaje de contacto</h3>
                <p><strong>Nombre:</strong> {dto.nombreRemitente}</p>
                <p><strong>Correo del remitente:</strong> {dto.correoRemitente}</p>
                <hr>
                <p><strong>Mensaje:</strong></p>
                <p>{dto.mensaje}</p>
            """
        }
        resend.Emails.send(params)
        print("DEBUG: Correo enviado exitosamente vía Resend")
    except Exception as e:
        print(f"ERROR en Resend: {str(e)}")

@app.post("/email/enviarCorreoContacto")
async def enviar_correo_contacto(email_dto: EmailDTO, background_tasks: BackgroundTasks):
    # Usamos BackgroundTasks para que la API responda "OK" de inmediato
    background_tasks.add_task(enviar_email_logic, email_dto)
    
    return {
        "message": "Tu mensaje está siendo procesado y se enviará en breve.",
        "success": True
    }

@app.get("/", include_in_schema=False)
async def root():
    from fastapi.responses import RedirectResponse
    return RedirectResponse(url="/docs")