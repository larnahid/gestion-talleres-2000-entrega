# =====================================================================
# DOCKERFILE - Backend Spring Boot GT2000
# GESTION TALLERES 2000 - TFG Nahid Larhziale Oullada
# =====================================================================

# Imagen base con JDK 17 y Maven
FROM maven:3.9-eclipse-temurin-17 AS builder

# Crear directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración Maven
COPY pom.xml .
COPY src ./src

# Construir la aplicación (skip tests para Docker build más rápido)
RUN mvn clean package -DskipTests

# Imagen final con JRE
FROM eclipse-temurin:17-jre

# Crear usuario no-root para seguridad
RUN groupadd -r gt2000 && useradd -r -g gt2000 gt2000

# Crear directorio para uploads
RUN mkdir -p /app/uploads && chown -R gt2000:gt2000 /app

# Cambiar al directorio de trabajo
WORKDIR /app

# Copiar el JAR del stage anterior
COPY --from=builder /app/target/*.jar app.jar

# Cambiar propietario del JAR
RUN chown gt2000:gt2000 app.jar

# Cambiar a usuario no-root
USER gt2000

# Exponer puerto 8080
EXPOSE 8080

# Variable de entorno para modo producción
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/api/auth/health || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]