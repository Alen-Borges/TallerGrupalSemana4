# HU - 010 Refined
# Validación de mensajes de reconocimiento por tipo de categoría

## Descripción

**Como** Empleado Sofkiano,
**quiero** que el sistema verifique que mi mensaje de agradecimiento cumple con los requisitos de la categoría seleccionada,
**para** asegurar que el reconocimiento sea relevante, de alta calidad y coherente con el tipo de premio otorgado.

---

## Criterios de aceptación

1. El Producer API debe validar el contenido del mensaje antes de aceptar la solicitud de envío de Kudo.
2. Si el mensaje no cumple con la longitud mínima o los términos requeridos por la categoría seleccionada, el sistema debe retornar un error `400` (Bad Request).
3. El sistema debe permitir la incorporación de nuevas reglas de validación para nuevas categorías sin necesidad de modificar el flujo principal de envío de mensajes.
4. El proceso de validación debe completarse antes de que el mensaje sea publicado en RabbitMQ y antes de emitir la confirmación `202` al usuario.
5. Los mensajes deben ser rechazados si contienen únicamente espacios en blanco o caracteres especiales sin sentido, independientemente de la categoría.

---

## Notas y preguntas abiertas

- El equipo debe definir la matriz de validación por categoría (ej. *Innovación*: mínimo 30 caracteres; *Trabajo en equipo*: debe mencionar un compañero) antes de la sesión de planeación.
- Se debe confirmar si los errores de validación se registrarán en un log de auditoría para analizar patrones de uso fallido.