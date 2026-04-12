# Etapa 1: Construcción (Usamos una imagen con Maven y Java 21)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# Copiamos los archivos de tu proyecto
COPY pom.xml .
COPY src ./src
# Compilamos el proyecto (igual que el comando que íbamos a poner en Render)
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (Usamos una imagen más ligera solo con Java 21 para correr la app)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copiamos el archivo .jar que se generó en la etapa anterior
COPY --from=build /app/target/backdigitalvertix-0.0.1-SNAPSHOT.jar app.jar
# Exponemos el puerto
EXPOSE 8080
# Le decimos cómo arrancar tu aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]