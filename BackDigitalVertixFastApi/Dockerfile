# Usamos una imagen ligera de Python 3.11
FROM python:3.11-slim

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo de dependencias e instalamos
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copiamos el resto del código
COPY . .

# Exponemos el puerto que usará FastAPI
EXPOSE 8080

# Comando para arrancar la API en producción
# Usamos '0.0.0.0' para que sea accesible externamente
CMD ["fastapi", "run", "main.py", "--port", "8080"]