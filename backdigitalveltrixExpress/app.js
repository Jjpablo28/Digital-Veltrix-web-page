const express = require('express');
const cors = require('cors');
const path = require('path'); // ✅ Se agregó esta línea
require('dotenv').config();

const app = express();

// Middlewares
app.use(cors());
app.use(express.json());
app.use(express.static(path.join(__dirname, 'public'))); // ✅ Ahora sí funcionará correctamente

// Rutas
const emailRouter = require('./routes/email');
app.use('/api', emailRouter);

// Ruta raíz opcional
app.get('/', (req, res) => {
  res.send('Servidor backend funcionando');
});

module.exports = app;
