# TEST_CASES_AI.md
# Matriz de Casos de Prueba — SofkianOS
## HU-010: Validación de Mensajes de Reconocimiento por Tipo de Categoría

---

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

---

## 2. Tabla de Ajustes Realizados por el Equipo

| ID | Caso generado por la Gema | Ajuste realizado por el equipo | ¿Por qué se ajustó? |
|----|--------------------------|-------------------------------|---------------------|
| TC-01 | Happy path con mensaje de 34 caracteres categoría TEAMWORK | Agregar validación de que el destinatario es distinto al remitente | La Gema omitió la invariante de Auto-Kudo incluso en el flujo feliz |
| TC-03 | Mensaje de exactamente 10 caracteres válido | El ejemplo "Buen trabajo" tiene 12 caracteres, no 10 | Error técnico de la Gema: el ejemplo no corresponde al valor límite declarado, debe usarse un mensaje de exactamente 10 caracteres |
| TC-05 | Rechazo de caracteres especiales sin alfanuméricos | Especificar que el rechazo ocurre en el Frontend antes de llegar al API | La validación de contenido debe aplicarse en dos capas: Frontend y Producer API, la Gema no diferenció las capas |
| TC-06 | Mención obligatoria por categoría | Eliminar este caso o marcarlo como pendiente | Las reglas de negocio del contexto del proyecto NO definen la regla de "mención obligatoria" para ninguna categoría, la Gema inventó una regla que no existe |
| TC-08 | Extensibilidad de nuevas categorías | Marcar como caso futuro fuera del MVP actual | El contexto del proyecto define solo 3 categorías fijas (TEAMWORK, INNOVATION, EXCELLENCE) y no hay roadmap de extensibilidad en el MVP |

---

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

> **Justificación TC-09:** Aplicando valores límite, si 10 es el mínimo válido, entonces 9 es el caso inválido obligatorio. La Gema cubrió el límite válido (TC-03) pero omitió el límite inválido inmediato.

> **Justificación TC-10:** El contexto del proyecto define que el Frontend debe tener feedback visual inmediato. La Gema solo validó el rechazo en el Producer API pero no probó la validación en la capa de UI, que es la primera línea de defensa.

---

## 4. Casos Eliminados y Justificación

| ID | Caso eliminado | Razón |
|----|---------------|-------|
| TC-06 | Mención obligatoria por categoría | Regla de negocio inexistente en el proyecto SofkianOS. La Gema generó una regla que no está definida en el contexto de negocio |
| TC-08 | Extensibilidad de nuevas categorías | Fuera del alcance del MVP. El proyecto define 3 categorías fijas y no hay roadmap de extensibilidad documentado |

---

## 5. Resumen de Cobertura

| Tipo de prueba | Casos cubiertos |
|---------------|----------------|
| Happy Path | TC-01, TC-03 |
| Negativos | TC-02, TC-04, TC-05, TC-09 |
| Edge Cases / Valores Límite | TC-03, TC-09 |
| Integración / Orden de capas | TC-07, TC-10 |
| Eliminados por el equipo | TC-06, TC-08 |
| Agregados por el equipo | TC-09, TC-10 |

---

## 6. Técnicas de Diseño Aplicadas

| Técnica | Casos donde se aplicó |
|---------|----------------------|
| **Particiones de equivalencia** | TC-01 (mensaje válido) vs TC-02 (mensaje muy corto) vs TC-04 (solo espacios) |
| **Valores límite** | TC-03 (10 chars válido) y TC-09 (9 chars inválido) |
| **Tablas de decisión** | TC-07 (orden de validación antes de RabbitMQ) |
| **Prueba por capas** | TC-05 y TC-10 (validación Frontend vs API) |

---

*Generado con Gema B — Generador de Casos de Prueba Gherkin Expert*
*Revisado y ajustado por el equipo QA — SofkianOS*
