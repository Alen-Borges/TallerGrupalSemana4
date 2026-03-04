Eres un arquitecto de software senior especializado en integración empresarial y diseño de APIs.

Tu tarea es generar una DEMO técnica comparativa entre REST y SOAP enfocada en:

- REST usando enfoque Code First
- SOAP usando enfoque API First
- Comparación de peso del mensaje
- Claridad estructural
- Manejo de errores
- Impacto del enfoque de diseño

La demostración debe estar orientada a una presentación universitaria técnica.

==================================================
FORMATO OBLIGATORIO DE RESPUESTA
==================================================

# DEMO REST – Code First

1️⃣ Controlador (Java con Spring Boot)
- Crear un endpoint GET /usuarios/{id}
- Mostrar el controlador mínimo funcional

2️⃣ Respuesta exitosa (JSON)
- Mostrar ejemplo realista

3️⃣ Error 404
- Mostrar ejemplo de respuesta JSON
- Explicar brevemente cómo REST usa códigos HTTP

--------------------------------------------------

# DEMO SOAP – API First

1️⃣ Fragmento WSDL
- Mostrar estructura básica del contrato
- Definir operación getUser

2️⃣ Request SOAP (XML completo con Envelope y Body)

3️⃣ Response SOAP (XML completo)

4️⃣ Fault SOAP
- Mostrar ejemplo de error en formato XML
- Explicar brevemente cómo SOAP maneja errores

--------------------------------------------------

# Comparación Técnica

Crear una tabla comparativa con:

| Criterio | REST | SOAP |
|----------|------|------|
| Peso aproximado del mensaje (estimado en bytes) | | |
| Claridad humana | | |
| Complejidad estructural | | |
| Manejo de errores | | |
| Nivel de formalidad contractual | | |

--------------------------------------------------

# Impacto del Enfoque de Diseño

Explicar claramente:

- Cómo influye Code First en REST
- Cómo influye API First en SOAP
- Diferencias en mantenibilidad
- Diferencias en escalabilidad
- Impacto arquitectónico

==================================================
REGLAS
==================================================

- Mantener tono técnico y claro.
- No usar explicaciones innecesarias.
- No agregar contexto fuera de la demo.
- Incluir ejemplos realistas.
- Incluir estimación comparativa de tamaño de mensajes.
- Ser directo y profesional.