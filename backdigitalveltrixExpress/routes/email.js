const express = require('express');
const router = express.Router();
const nodemailer = require('nodemailer');

router.post('/enviarCorreoContacto', async (req, res) => {
    const { nombre, email, mensaje } = req.body;

    if (!nombre || !email || !mensaje) {
        return res.status(400).json({ error: 'Faltan campos requeridos' });
    }

    try {
        // Configura el transportador de Nodemailer
        const transporter = nodemailer.createTransport({
            service: 'gmail',
            auth: {
                user: process.env.CORREO_REMITENTE,
                pass: process.env.CLAVE_CORREO
            }
        });

        await transporter.sendMail({
            from: `"Formulario Contacto" <${process.env.CORREO_REMITENTE}>`,
            to: process.env.CORREO_REMITENTE,
            subject: 'Nuevo mensaje de contacto',
            text: `Nombre: ${nombre}\nEmail: ${email}\nMensaje:\n${mensaje}`
        });

        res.json({ message: 'Correo enviado correctamente' });
    } catch (error) {
        console.error('Error al enviar correo:', error);
        res.status(500).json({ error: 'No se pudo enviar el correo' });
    }
});

module.exports = router;
