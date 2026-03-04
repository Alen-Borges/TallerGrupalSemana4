# TEST_CASES_AI.md
# Matriz de Casos de Prueba — SofkianOS
## US-006: Dockerización multi-stage de frontend React para despliegue ligero con Nginx

---

## 1. Casos de Prueba Generados por la Gema B

```gherkin
Feature: Dockerización y Despliegue Frontend SofkianOS

  Scenario: TC-01 Construcción exitosa de artefactos de producción (Happy Path)
    Given que el código fuente de SofkianOS (React 19.2.0) está en un contenedor con Node.js 20 Alpine
    When se ejecuta el comando de construcción de Vite 7.2.4
    Then se debe crear la carpeta /dist/ conteniendo el archivo index.html
    And los recursos JS y CSS deben estar minificados

  Scenario: TC-02 Transferencia correcta de archivos a la etapa de servicio
    Given que la etapa de construcción (builder) ha finalizado exitosamente
    When se inicia la etapa de producción basada en Nginx Alpine
    Then los archivos de la carpeta /dist/ deben copiarse a /usr/share/nginx/html/ dentro de la imagen final

  Scenario: TC-03 Verificación de limpieza y peso de la imagen final
    Given que la imagen de producción ha sido generada mediante el build multi-stage
    When se inspecciona el sistema de archivos de la imagen final
    Then no deben existir rastros de la carpeta node_modules
    And no deben existir archivos .ts ni .tsx
    And el binario de Node.js no debe estar presente en el PATH
    And el peso total de la imagen debe ser menor a 50MB

  Scenario: TC-04 Manejo de rutas SPA (Fallback a index.html)
    Given que un usuario intenta acceder directamente a una ruta interna /enviar-kudo
    When el servidor Nginx recibe la petición de una ruta que no existe físicamente
    Then Nginx debe responder con el archivo index.html
    And el código de estado HTTP debe ser 200

  Scenario: TC-05 Validación de disponibilidad del servicio en puerto 80
    Given que el contenedor de Docker de SofkianOS está en ejecución
    When se realiza una petición HTTP GET a la dirección IP del host en el puerto 80
    Then el servidor debe retornar una respuesta exitosa
    And el encabezado Server debe identificar explícitamente a nginx

  Scenario: TC-06 Aislamiento de dependencias de desarrollo en el build
    Given que el archivo package.json contiene devDependencies y dependencies
    When se realiza la instalación de paquetes en la etapa de construcción
    Then solo los artefactos necesarios para la ejecución estática deben persistir en la imagen de Nginx
    And las herramientas de desarrollo no deben estar presentes tras el build

  Scenario: TC-07 Consistencia de versiones en el entorno de compilación
    Given que el proyecto requiere Node.js LTS según el requerimiento técnico RT-007
    When se inicia el proceso de Docker build
    Then la imagen base utilizada debe ser estrictamente node:20-alpine
```

---

## 2. Tabla de Ajustes Realizados por el Equipo

| ID | Caso generado por la Gema | Ajuste realizado por el equipo | ¿Por qué se ajustó? |
|----|--------------------------|-------------------------------|---------------------|
| TC-01 | Construcción exitosa del build con React y Vite | Agregar validación de que el build falla si hay errores de TypeScript | La Gema no consideró que errores de compilación deben detener el proceso y no generar el /dist/ |
| TC-02 | Transferencia de archivos /dist/ a /usr/share/nginx/html/ | Verificar también que el `nginx.conf` personalizado esté presente en la imagen | Sin el nginx.conf correcto, el enrutamiento SPA falla aunque los estáticos estén copiados |
| TC-03 | Limpieza de imagen y peso menor a 50MB | Agregar validación de que archivos `.env` y secretos no estén presentes en la imagen | La Gema olvidó que archivos de configuración sensibles no deben existir en la imagen de producción por seguridad |
| TC-05 | Validación de respuesta en puerto 80 con encabezado Server=nginx | Agregar validación del encabezado `Content-Type: text/html` en la respuesta | No basta con que responda 200, debe servir HTML correcto para confirmar que sirve la SPA |
| TC-07 | Hardcodeo de versión node:20-alpine | **Eliminar este caso** | Las notas técnicas del proyecto indican explícitamente que las versiones NO deben hardcodearse en los criterios de aceptación ante posibles actualizaciones |

---

## 3. Casos de Prueba Adicionales (Agregados por el Equipo)

```gherkin
  Scenario: TC-08 Fallo del build por errores de compilación TypeScript
    Given que el código fuente contiene errores de TypeScript
    When se ejecuta el comando de build de Vite
    Then el proceso debe fallar con un código de salida distinto a 0
    And no debe generarse el directorio /dist/

  Scenario: TC-09 Verificación que el proxy /api no existe en producción
    Given que el contenedor de producción está en ejecución
    When se realiza una petición HTTP a la ruta /api desde el contenedor
    Then Nginx debe retornar código de estado 404
    And no debe redirigir la petición al backend
```

> **Justificación TC-08:** La Gema solo cubrió el happy path del build. Es crítico validar que un error de compilación detiene el proceso y no genera una imagen rota.

> **Justificación TC-09:** Según la nota técnica RT-004 del proyecto, el proxy `/api` hacia el backend aplica ÚNICAMENTE en desarrollo. La Gema no consideró esta regla de negocio técnica.

---

## 4. Resumen de Cobertura

| Tipo de prueba | Casos cubiertos |
|---------------|----------------|
| Happy Path | TC-01, TC-02, TC-05, TC-06 |
| Negativos | TC-08, TC-09 |
| Edge Cases / Seguridad | TC-03, TC-04 |
| Eliminados por el equipo | TC-07 |
| Agregados por el equipo | TC-08, TC-09 |

---

*Generado con Gema B — Generador de Casos de Prueba Gherkin Expert*
*Revisado y ajustado por el equipo QA — SofkianOS*
