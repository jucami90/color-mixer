# color-mixer

Calculadora de mezcla de colores con Spring Boot 3 y Java 21. Mezcla 2 o 3 colores en dos modos:

- **PINTURA** (pigmentos, modelo RYB): rojo + azul = morado, amarillo + azul = verde, rojo + amarillo = naranja.
- **LUZ** (pantallas, RGB aditivo): rojo + verde = amarillo, rojo + verde + azul = blanco.

## Ejecutar en local

```bash
mvn spring-boot:run
# Web:  http://localhost:8080
# API:  http://localhost:8080/api/colores/mezclar?colores=amarillo,azul&modo=pintura
```

Con Docker:

```bash
docker build -t color-mixer .
docker run -p 8080:8080 color-mixer
```

## Pruebas

| Comando | Qué ejecuta |
|---|---|
| `mvn test` | Pruebas unitarias (JUnit 5 + AssertJ, parametrizadas) |
| `mvn verify` | Unitarias + integración (`*IT.java`, MockMvc) + cobertura mínima del 80 % (JaCoCo) |
| `mvn test-compile org.pitest:pitest-maven:mutationCoverage` | Pruebas de mutación (PIT) |
| `npx newman run postman/color-mixer.postman_collection.json -e postman/local.postman_environment.json` | Pruebas de API con Postman (con la app arrancada) |

Informes: `target/site/jacoco/index.html` y `target/pit-reports/index.html`.

### Postman

La carpeta `postman/` tiene la colección y el entorno local. Impórtalos en Postman (*Import*) y ejecútalos con *Run collection* mientras la app está arrancada.

## Pipelines

- `.github/workflows/ci.yml`: compilación, pruebas, cobertura, mutación y escaneo de dependencias (Trivy).
- `.github/workflows/codeql.yml`: análisis estático de seguridad (CodeQL).
- `.github/workflows/cd.yml`: imagen Docker → prueba de humo → escaneo → publicación en GHCR/Docker Hub → despliegue.
- `.github/dependabot.yml`: actualización automática de dependencias.
- `.circleci/config.yml`: pipeline de la actividad 1 adaptado a este proyecto.
