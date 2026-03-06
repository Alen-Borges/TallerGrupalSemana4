# TEST_CASES_AI.md
# Matriz de Casos de Prueba — SofkianOS
### Proyecto: SofkianOS — Sistema Operativo de Kudos
### Historias analizadas: US-006 | HU-004 | HU-010

---

# 📋 ÍNDICE

1. [US-006 — Dockerización multi-stage de frontend React](#us-006)
2. [HU-004 — Estandarización de Categorías de Reconocimiento](#hu-004)
3. [HU-010 — Validación de Mensajes por Tipo de Categoría](#hu-010)
4. [Resumen Global del Proyecto](#resumen)

---

<a name="us-006"></a>
# US-006 — Dockerización multi-stage de frontend React para despliegue ligero con Nginx

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

## 2. Tabla de Ajustes Realizados por el Equipo

| ID | Caso generado por la Gema | Ajuste realizado por el equipo | ¿Por qué se ajustó? |
|----|--------------------------|-------------------------------|---------------------|
| TC-01 | Construcción exitosa del build con React y Vite | Agregar validación de que el build falla si hay errores de TypeScript | La Gema no consideró que errores de compilación deben detener el proceso y no generar el /dist/ |
| TC-02 | Transferencia de archivos /dist/ a /usr/share/nginx/html/ | Verificar también que el `nginx.conf` personalizado esté presente en la imagen | Sin el nginx.conf correcto, el enrutamiento SPA falla aunque los estáticos estén copiados |
| TC-03 | Limpieza de imagen y peso menor a 50MB | Agregar validación de que archivos `.env` y secretos no estén presentes en la imagen | La Gema olvidó que archivos de configuración sensibles no deben existir en la imagen de producción por seguridad |
| TC-05 | Validación de respuesta en puerto 80 con encabezado Server=nginx | Agregar validación del encabezado `Content-Type: text/html` en la respuesta | No basta con que responda 200, debe servir HTML correcto para confirmar que sirve la SPA |
| TC-07 | Hardcodeo de versión node:20-alpine | **Eliminar este caso** | Las notas técnicas del proyecto indican explícitamente que las versiones NO deben hardcodearse ante posibles actualizaciones |

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

## 4. Resumen de Cobertura US-006

| Tipo de prueba | Casos cubiertos |
|---------------|----------------|
| Happy Path | TC-01, TC-02, TC-05, TC-06 |
| Negativos | TC-08, TC-09 |
| Edge Cases / Seguridad | TC-03, TC-04 |
| Eliminados por el equipo | TC-07 |
| Agregados por el equipo | TC-08, TC-09 |

---

<a name="hu-004"></a>
# HU-004 — Estandarización de Categorías de Reconocimiento

## 1. Casos de Prueba Generados por la Gema B

```gherkin
Feature: Estandarización de categorías de reconocimiento en SofkianOS

  Scenario: TC-01 Envío exitoso de Kudo con categoría válida (Happy Path)
    Given que un Empleado Sofka se encuentra en el formulario de envío de Kudos
    And ha ingresado un destinatario y un mensaje de 15 caracteres
    When selecciona la categoría "TEAMWORK" de la lista predefinida y envía el formulario
    Then el sistema debe mostrar un mensaje de éxito
    And persistir la categoría seleccionada
    And visualizarla en el muro público

  Scenario: TC-02 Validación de campo de categoría obligatorio en Frontend
    Given que el Empleado Sofka ha completado todos los campos del formulario excepto la categoría
    When intenta enviar el reconocimiento
    Then el sistema debe impedir el envío
    And mostrar un aviso de validación indicando que la selección de categoría es obligatoria

  Scenario: TC-03 Validación de mensaje con límite inferior exacto (10 caracteres)
    Given que el Empleado Sofka selecciona la categoría "INNOVATION"
    When escribe un mensaje de exactamente 10 caracteres y envía el formulario
    Then el sistema debe aceptar la solicitud
    And retornar un código de respuesta 202
    And procesar los 15 puntos asociados a la categoría INNOVATION

  Scenario: TC-04 Intento de envío de categoría inválida mediante API
    Given que se realiza una petición directa al Producer API
    When el cuerpo de la solicitud contiene una categoría inexistente como "MAGIC"
    Then el sistema debe responder con código de error 400
    And detallar que la categoría no pertenece al catálogo autorizado

  Scenario: TC-05 Restricción de Auto-Kudo con categoría seleccionada
    Given que un Empleado Sofka intenta enviarse un reconocimiento a sí mismo
    When selecciona la categoría "EXCELLENCE" e intenta finalizar el proceso
    Then el sistema debe rechazar la acción
    And mostrar mensaje basado en la invariante de Auto-Kudo

  Scenario: TC-06 Indisponibilidad del Message Broker (RabbitMQ)
    Given que el servicio de mensajería RabbitMQ no se encuentra disponible
    When el usuario intenta enviar un Kudo con una categoría válida
    Then el Producer API debe informar al usuario
    And mostrar mensaje de que el servicio no está disponible temporalmente

  Scenario: TC-07 Persistencia y visualización de puntos según categoría
    Given que se ha enviado exitosamente un Kudo con la categoría "EXCELLENCE"
    When el Consumer Worker procesa el evento en segundo plano
    Then se deben asignar 20 puntos al destinatario
    And registrar la transacción según la escala definida

  Scenario: TC-08 Gestión de fallos en el procesamiento (Dead Letter Queue)
    Given que un Kudo con categoría válida fue aceptado por la API
    And ocurre un error de base de datos durante el procesamiento
    When el sistema detecta la falla de persistencia
    Then el Kudo debe ser movido automáticamente a la cola "kudos.dlq"
    And quedar disponible para revisión manual posterior
```

## 2. Tabla de Ajustes Realizados por el Equipo

| ID | Caso generado por la Gema | Ajuste realizado por el equipo | ¿Por qué se ajustó? |
|----|--------------------------|-------------------------------|---------------------|
| TC-01 | Envío exitoso con categoría TEAMWORK y mensaje de 15 caracteres | Agregar validación de que el destinatario es diferente al remitente | La Gema no validó explícitamente la invariante de Auto-Kudo en el happy path |
| TC-02 | Validación de categoría obligatoria en Frontend | Agregar validación de que el error se muestra en tiempo real sin necesidad de enviar | La UX del proyecto exige feedback visual inmediato, no solo al intentar enviar |
| TC-03 | Mensaje de exactamente 10 caracteres con INNOVATION | Agregar caso paralelo con 9 caracteres que debe fallar | La Gema cubrió el valor límite válido pero omitió el valor límite inválido (9 caracteres) |
| TC-04 | Categoría inválida "MAGIC" por API | Agregar validación del mensaje de error descriptivo en el body de la respuesta | No basta con el código 400, el sistema debe indicar qué categorías son válidas |
| TC-07 | Persistencia de 20 puntos con categoría EXCELLENCE | Verificar también TEAMWORK=10 e INNOVATION=15 | La Gema solo probó una categoría, deben validarse las 3 escalas de puntos definidas |

## 3. Casos de Prueba Adicionales (Agregados por el Equipo)

```gherkin
  Scenario: TC-09 Validación de mensaje con 9 caracteres (valor límite inválido)
    Given que el Empleado Sofka selecciona la categoría "TEAMWORK"
    When escribe un mensaje de exactamente 9 caracteres y envía el formulario
    Then el sistema debe rechazar el envío
    And mostrar mensaje indicando que el mensaje debe tener mínimo 10 caracteres

  Scenario: TC-10 Verificación de escala de puntos para las 3 categorías
    Given que existen tres categorías definidas TEAMWORK, INNOVATION y EXCELLENCE
    When se procesa un Kudo exitoso de cada categoría
    Then TEAMWORK debe asignar exactamente 10 puntos al destinatario
    And INNOVATION debe asignar exactamente 15 puntos al destinatario
    And EXCELLENCE debe asignar exactamente 20 puntos al destinatario
```

> **Justificación TC-09:** Aplicando la técnica de valores límite, si 10 es el mínimo válido, entonces 9 debe ser el caso inválido obligatorio a probar. La Gema omitió este caso crítico.

> **Justificación TC-10:** La Gema solo validó la categoría EXCELLENCE en TC-07. Las reglas de negocio definen 3 escalas distintas de puntos y todas deben ser verificadas para garantizar la correcta implementación de la gamificación.

## 4. Resumen de Cobertura HU-004

| Tipo de prueba | Casos cubiertos |
|---------------|----------------|
| Happy Path | TC-01, TC-03, TC-07 |
| Negativos | TC-02, TC-04, TC-05, TC-06, TC-09 |
| Edge Cases / Valores Límite | TC-03, TC-09 |
| Integración / Backend | TC-06, TC-08 |
| Agregados por el equipo | TC-09, TC-10 |

---

<a name="hu-010"></a>
# HU-010 — Validación de Mensajes de Reconocimiento por Tipo de Categoría

## 1. Casos de Prueba Generados por la Gema B

```gherkin
Feature: Validación de mensajes de reconocimiento por tipo de categoría

  Scenario: TC-01 Validación exitosa de mensaje para categoría estándar (Happy Path)
    Given que un Empleado Sofka ha seleccionado la categoría "TEAMWORK"
    And el mensaje ingresado contiene "Gracias por tu apoyo en el sprint" con 34 caracteres
    When intenta enviar el Kudo a través del Producer API
    Then el sistema debe validar el contenido
    And retornar un código de respuesta 202
    And publicar el evento en RabbitMQ

  Scenario: TC-02 Rechazo de mensaje por longitud mínima insuficiente
    Given que el usuario selecciona la categoría "INNOVATION"
    When ingresa un mensaje de 5 caracteres "Genio"
    Then el Producer API debe rechazar la solicitud con error 400
    And detallar que el mensaje no cumple la longitud mínima requerida

  Scenario: TC-03 Validación de mensaje con valor límite inferior exacto (10 caracteres)
    Given que el sistema requiere un mínimo de 10 caracteres para cualquier Kudo
    When el usuario escribe un mensaje de exactamente 10 caracteres
    And la categoría seleccionada no tiene restricciones adicionales de longitud
    Then el sistema debe aceptar el mensaje
    And procesar la solicitud exitosamente

  Scenario: TC-04 Rechazo de mensajes compuestos solo por espacios en blanco
    Given que un Empleado Sofka completa los campos obligatorios del Kudo
    When ingresa un mensaje que consiste únicamente en 15 espacios en blanco
    Then el sistema debe rechazar el envío con error 400 por contenido inválido

  Scenario: TC-05 Rechazo de mensajes con caracteres especiales sin contenido alfanumérico
    Given que el usuario intenta enviar un reconocimiento
    When ingresa como mensaje una cadena de símbolos sin texto alfanumérico "@#$%^&*!!!"
    Then el sistema debe invalidar la solicitud
    And no debe publicar el mensaje en el Message Broker

  Scenario: TC-06 Validación de regla específica por categoría (Mención obligatoria)
    Given que la categoría seleccionada tiene como regla mencionar un compañero
    When el usuario envía un mensaje que no incluye el nombre o tag de un colaborador
    Then el sistema debe retornar un error 400
    And indicar el incumplimiento de la regla de la categoría

  Scenario: TC-07 Verificación de orden de validación antes de RabbitMQ
    Given que el Producer API recibe una solicitud de Kudo con un mensaje que viola las reglas
    When se ejecuta el flujo de validación
    Then el sistema debe emitir el error 400
    And no debe existir ningún mensaje nuevo en la cola de RabbitMQ

  Scenario: TC-08 Extensibilidad de nuevas reglas de categoría
    Given que se incorpora una nueva categoría al catálogo autorizado
    And se define una regla de validación de 50 caracteres para dicha categoría
    When un usuario envía un mensaje de 40 caracteres para esta nueva categoría
    Then el sistema debe rechazar la solicitud
    And no debe requerir cambios en el flujo principal de envío
```

## 2. Tabla de Ajustes Realizados por el Equipo

| ID | Caso generado por la Gema | Ajuste realizado por el equipo | ¿Por qué se ajustó? |
|----|--------------------------|-------------------------------|---------------------|
| TC-01 | Happy path con mensaje de 34 caracteres categoría TEAMWORK | Agregar validación de que el destinatario es distinto al remitente | La Gema omitió la invariante de Auto-Kudo incluso en el flujo feliz |
| TC-03 | Mensaje de exactamente 10 caracteres válido | El ejemplo "Buen trabajo" tiene 12 caracteres, no 10 | Error técnico de la Gema: el ejemplo no corresponde al valor límite declarado |
| TC-05 | Rechazo de caracteres especiales sin alfanuméricos | Especificar que el rechazo ocurre en el Frontend antes de llegar al API | La validación debe aplicarse en dos capas: Frontend y Producer API |
| TC-06 | Mención obligatoria por categoría | **Eliminar este caso** | Las reglas de negocio NO definen la regla de "mención obligatoria", la Gema inventó una regla inexistente |
| TC-08 | Extensibilidad de nuevas categorías | **Eliminar este caso** | Fuera del alcance del MVP. El proyecto define solo 3 categorías fijas |

## 3. Casos de Prueba Adicionales (Agregados por el Equipo)

```gherkin
  Scenario: TC-09 Rechazo de mensaje con 9 caracteres (valor límite inválido)
    Given que el Empleado Sofka selecciona la categoría "EXCELLENCE"
    When escribe un mensaje de exactamente 9 caracteres
    And intenta enviar el formulario
    Then el sistema debe rechazar el envío con error 400
    And mostrar mensaje indicando que el mínimo es 10 caracteres

  Scenario: TC-10 Verificación que mensajes inválidos no llegan a RabbitMQ (capa Frontend)
    Given que el Empleado Sofka ingresa un mensaje de 5 caracteres en el formulario
    When intenta enviar el Kudo desde la interfaz web
    Then el Frontend debe mostrar error de validación inmediatamente
    And no debe realizarse ninguna petición HTTP al Producer API
```

> **Justificación TC-09:** Aplicando valores límite, si 10 es el mínimo válido, entonces 9 es el caso inválido obligatorio. La Gema cubrió el límite válido pero omitió el límite inválido inmediato.

> **Justificación TC-10:** El proyecto define feedback visual inmediato en el Frontend. La Gema solo validó el rechazo en el Producer API sin probar la capa de UI.

## 4. Casos Eliminados y Justificación

| ID | Caso eliminado | Razón |
|----|---------------|-------|
| TC-06 | Mención obligatoria por categoría | Regla de negocio inexistente en SofkianOS. La Gema generó una regla no definida en el contexto de negocio |
| TC-08 | Extensibilidad de nuevas categorías | Fuera del alcance del MVP. El proyecto define 3 categorías fijas sin roadmap de extensibilidad documentado |

## 5. Resumen de Cobertura HU-010

| Tipo de prueba | Casos cubiertos |
|---------------|----------------|
| Happy Path | TC-01, TC-03 |
| Negativos | TC-02, TC-04, TC-05, TC-09 |
| Edge Cases / Valores Límite | TC-03, TC-09 |
| Integración / Orden de capas | TC-07, TC-10 |
| Eliminados por el equipo | TC-06, TC-08 |
| Agregados por el equipo | TC-09, TC-10 |

---

<a name="resumen"></a>
# 📊 RESUMEN GLOBAL DEL PROYECTO

| Historia | Generados por Gema | Eliminados | Ajustados | Agregados | Total final |
|----------|--------------------|------------|-----------|-----------|-------------|
| US-006 | 7 | 1 | 4 | 2 | 8 |
| HU-004 | 8 | 0 | 5 | 2 | 10 |
| HU-010 | 8 | 2 | 4 | 2 | 8 |
| **Total** | **23** | **3** | **13** | **6** | **26** |

## Técnicas de Diseño Aplicadas (Global)

| Técnica | Historias donde se aplicó |
|---------|--------------------------|
| **Particiones de equivalencia** | HU-004, HU-010 |
| **Valores límite** | HU-004, HU-010 |
| **Tablas de decisión** | HU-004, HU-010 |
| **Prueba por capas (Frontend vs API)** | HU-010 |
| **Análisis de reglas de negocio** | US-006, HU-004, HU-010 |

---

*Generado con Gema B — Generador de Casos de Prueba Gherkin Expert*
*Revisado y ajustado críticamente por el equipo QA — SofkianOS*
