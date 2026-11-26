# ---------- Etapa 1: compilación ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
# Las pruebas ya se ejecutan en el pipeline de CI; aquí solo empaquetamos
RUN mvn -B -q package -DskipTests

# ---------- Etapa 2: imagen final mínima ----------
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Usuario sin privilegios (no ejecutar como root)
RUN addgroup -S app && adduser -S app -G app
COPY --from=build /app/target/color-mixer.jar app.jar
USER app

EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
