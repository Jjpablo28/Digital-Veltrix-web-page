const nodemailer = require('nodemailer');

exports.enviarCorreoContacto = async (req, res) => {
    const { nombre, correo, mensaje } = req.body;

    // Validación simple
    if (!nombre || !correo || !mensaje) {
        return res.status(400).json({ error: 'Faltan campos requeridos' });
    }

    try {
        const transporter = nodemailer.createTransport({
            service: 'gmail',
            auth: {
                user: process.env.EMAIL_USER,
                pass: process.env.EMAIL_PASS
            }
        });

        const mailOptions = {
            from: correo,
            to: process.env.EMAIL_USER,
            subject: `Nuevo mensaje de contacto de ${nombre}`,
            text: `
        Nombre: ${nombre}
        Correo: ${correo}
        Mensaje:
        ${mensaje}
      `
        };

        await transporter.sendMail(mailOptions);
        res.status(200).json({ mensaje: 'Correo enviado exitosamente' });
    } catch (error) {
        console.error('Error al enviar correo:', error);
        res.status(500).json({ error: 'Error al enviar el correo' });
    }
};
