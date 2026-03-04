# HU - 004 Refined

# Estandarización de categorías de reconocimiento en el envío de Kudos

## Descripción

**Como** Empleado Sofkian,
**quiero** seleccionar una categoría de una lista predefinida al enviar un reconocimiento,
**para** asegurar la consistencia en los criterios de premiación y garantizar la correcta asignación de puntos en el sistema de gamificación.

---

## Criterios de aceptación

- El formulario de envío de Kudos debe incluir un componente de selección obligatoria que contenga las categorías fijas definidas por el negocio.
- El sistema debe impedir el envío del formulario y mostrar un aviso de validación si el usuario no ha seleccionado una categoría de la lista.
- El Producer API debe validar que la categoría recibida pertenezca al catálogo autorizado antes de retornar el código `202` y publicar el evento en el Message Broker.
- Si se recibe una categoría inexistente o inválida a través de la API, el sistema debe responder con un error de solicitud incorrecta (`400`) detallando el fallo.
- La categoría seleccionada debe ser persistida y visualizada claramente en el muro público junto con el mensaje del reconocimiento.

---

## Notas y preguntas abiertas

- Dado que las categorías estarán quemadas en el código por ahora debido a las limitaciones del MVP, se debe documentar su ubicación para facilitar futuras actualizaciones.
- El equipo debe acordar si se mostrarán iconos representativos para cada categoría para fortalecer la identidad visual.
- Se asume que no hay autenticación todavía, por lo que la validación de la categoría es el control principal de integridad en este flujo.