# TEST_CASES_AI.md
# Matriz de Casos de Prueba — SofkianOS
## HU-004: Estandarización de Categorías de Reconocimiento

---

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

---

## 2. Tabla de Ajustes Realizados por el Equipo

| ID | Caso generado por la Gema | Ajuste realizado por el equipo | ¿Por qué se ajustó? |
|----|--------------------------|-------------------------------|---------------------|
| TC-01 | Envío exitoso con categoría TEAMWORK y mensaje de 15 caracteres | Agregar validación de que el destinatario es diferente al remitente | La Gema no validó explícitamente la invariante de Auto-Kudo en el happy path |
| TC-02 | Validación de categoría obligatoria en Frontend | Agregar validación de que el error se muestra en tiempo real (sin necesidad de enviar) | La UX del proyecto exige feedback visual inmediato, no solo al intentar enviar |
| TC-03 | Mensaje de exactamente 10 caracteres con INNOVATION | Agregar caso paralelo con 9 caracteres que debe fallar | La Gema cubrió el valor límite válido pero omitió el valor límite inválido (9 caracteres) |
| TC-04 | Categoría inválida "MAGIC" por API | Agregar validación del mensaje de error descriptivo en el body de la respuesta | No basta con el código 400, el sistema debe indicar qué categorías son válidas |
| TC-07 | Persistencia de 20 puntos con categoría EXCELLENCE | Verificar también TEAMWORK=10 e INNOVATION=15 | La Gema solo probó una categoría, deben validarse las 3 escalas de puntos definidas |

---

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

---

## 4. Resumen de Cobertura

| Tipo de prueba | Casos cubiertos |
|---------------|----------------|
| Happy Path | TC-01, TC-03, TC-07 |
| Negativos | TC-02, TC-04, TC-05, TC-06, TC-09 |
| Edge Cases / Valores Límite | TC-03, TC-09 |
| Integración / Backend | TC-06, TC-08 |
| Agregados por el equipo | TC-09, TC-10 |

---

## 5. Técnicas de Diseño Aplicadas

| Técnica | Casos donde se aplicó |
|---------|----------------------|
| **Particiones de equivalencia** | TC-01 (categoría válida) vs TC-04 (categoría inválida) |
| **Valores límite** | TC-03 (10 chars válido) y TC-09 (9 chars inválido) |
| **Tablas de decisión** | TC-06 (broker caído) y TC-08 (falla de BD) |
| **Reglas de negocio** | TC-05 (Auto-Kudo) y TC-10 (escala de puntos) |

---

*Generado con Gema B — Generador de Casos de Prueba Gherkin Expert*
*Revisado y ajustado por el equipo QA — SofkianOS*
