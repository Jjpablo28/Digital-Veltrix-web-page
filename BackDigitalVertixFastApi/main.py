import resend
import os

# La clave de API reemplazaría a tu EMAIL_PASSWORD en Render
resend.api_key = os.getenv("RESEND_API_KEY")

def enviar_email_logic(dto: EmailDTO):
    # Esta petición viaja por el puerto web seguro, saltándose el bloqueo
    resend.Emails.send({
        "from": "onboarding@resends.dev", # Dominio de pruebas o tu propio dominio verificado
        "to": "veltrixdigital.co@gmail.com",
        "subject": f"Mensaje de Contacto: {dto.asunto}",
        "html": f"<h3>Nuevo mensaje</h3><p>De: {dto.nombreRemitente} ({dto.correoRemitente})</p><p>{dto.mensaje}</p>"
    })