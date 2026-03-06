# Informe de Refinamiento de Historias de Usuario

Este documento presenta el informe comparativo que incluye las 3 Historias de Usuario analizadas en el proyecto: la versión original de cada una, la versión refinada por la Gema que diagnostica y refina historias de usuario, y el cuadro de diferencias y justificación de los ajustes técnicos.

---

## Cuadro de Diferencias Detectadas

| Historia de Usuario | Comentario y Diferencias Detectadas |
|---------------------|--------------------------------------|
| **HU-004** | Se evidencia el uso de una descripción clara (quién quiere la funcionalidad, qué quiere y para qué), se añaden criterios de aceptación claros y se dejan notas y preguntas abiertas. |
| **HU-006 (US-006)** | Se eliminan detalles como especificaciones de arquitectura que no se sugieren en este documento, la matriz de calidad INVEST es eliminada porque se debe inferir que esta HU ya cumple con estos principios o debía pasar por un refinamiento cuando ya está lista. Los escenarios Gherkin también se eliminan porque estos serán definidos en una fase siguiente a las HU. |
| **HU-010** | Se evidencia el uso de una descripción estructurada (quién, qué, para qué), se añaden criterios de aceptación rigurosos y se dejan documentadas las notas y preguntas abiertas. |

---

## Detalle de Historias de Usuario Analizadas

### 1. HU-004: Estandarización de categorías de reconocimiento

#### Versión Original
> Quiero que los reconocimientos que damos (kudos) tengan categorías claras y fijas, para que no haya errores al escribirlas y todos usemos los mismos criterios al premiar el talento de nuestros compañeros.

#### Versión Refinada (Generada por Gema)
**Título:** Estandarización de categorías de reconocimiento en el envío de Kudos

**Descripción:**
**Como** Empleado Sofkian,
**quiero** seleccionar una categoría de una lista predefinida al enviar un reconocimiento,
**para** asegurar la consistencia en los criterios de premiación y garantizar la correcta asignación de puntos en el sistema de gamificación.

**Criterios de aceptación:**
- El formulario de envío de Kudos debe incluir un componente de selección obligatoria que contenga las categorías fijas definidas por el negocio.
- El sistema debe impedir el envío del formulario y mostrar un aviso de validación si el usuario no ha seleccionado una categoría de la lista.
- El Producer API debe validar que la categoría recibida pertenezca al catálogo autorizado antes de retornar el código `202` y publicar el evento en el Message Broker.
- Si se recibe una categoría inexistente o inválida a través de la API, el sistema debe responder con un error de solicitud incorrecta (`400`) detallando el fallo.
- La categoría seleccionada debe ser persistida y visualizada claramente en el muro público junto con el mensaje del reconocimiento.

**Notas y preguntas abiertas:**
- Dado que las categorías estarán quemadas en el código por ahora debido a las limitaciones del MVP, se debe documentar su ubicación para facilitar futuras actualizaciones.
- El equipo debe acordar si se mostrarán iconos representativos para cada categoría para fortalecer la identidad visual.
- Se asume que no hay autenticación todavía, por lo que la validación de la categoría es el control principal de integridad en este flujo.

---

### 2. HU-006 (US-006): Dockerización Multi-Stage del Frontend

#### Versión Original
**Título:** US-006 Dockerización Multi-Stage del Frontend

**1. Definición de la HU**
**Como** persona de operaciones
**Quiero** dockerizar la aplicación frontend utilizando un build multi-stage
**Para** compilar el código fuente aislado y servir estáticos de forma ultraligera en producción.

**2. Especificaciones de Arquitectura y Despliegue**
* **Capa de Arquitectura:** Infraestructura (Contenedores)
* **Herramientas:** Docker, Node.js 20 (Alpine), Nginx (Alpine).
* **Estrategia de Despliegue:** Multi-Stage Build estático.

*(Se incluía también una matriz INVEST de calidad, validación Gherkin y Definition of Done, los cuales fueron abstraídos en la refinación para alinearse a la estructura estándar de HU).*

#### Versión Refinada (Generada por Gema)
**Título:** Optimización y contenedorización del frontend de SofkianOS para despliegue productivo

**Descripción:**
**Como** desarrollador del equipo de infraestructura,
**quiero** automatizar la creación de una imagen Docker liviana mediante una construcción multi-etapa,
**para** reducir los tiempos de despliegue, minimizar la superficie de ataque y asegurar que el frontend se sirva eficientemente mediante Nginx.

**Criterios de aceptación:**
- El `Dockerfile` debe utilizar una etapa de construcción inicial basada en **Node JS LTS** para compilar la aplicación React y transformar el código TypeScript.
- La etapa final de ejecución debe utilizar una imagen de **Nginx Alpine** para servir únicamente los archivos estáticos generados en el build.
- El contenedor resultante no debe incluir el código fuente, dependencias de desarrollo ni herramientas del sistema operativo que no sean estrictamente necesarias.
- Se debe incluir un archivo `.dockerignore` para evitar que la carpeta `node_modules` local y otros archivos de configuración innecesarios se copien al contexto de construcción.
- La configuración de Nginx dentro del contenedor debe permitir la recarga de rutas directas para soportar el funcionamiento de **React Router** sin errores de archivo no encontrado.
- La imagen debe integrarse correctamente en el archivo `docker-compose` del proyecto y responder en el puerto configurado sin bloqueos.

**Notas y preguntas abiertas:**
- El equipo debe decidir si el escaneo de vulnerabilidades de la imagen se realizará dentro del `Dockerfile` o como un paso independiente en el pipeline de Jenkins.
- Queda pendiente definir si se usará una imagen espejo interna para las dependencias de Node por motivos de seguridad corporativa.

---

### 3. HU-010: Validación de mensajes de reconocimiento por tipo de categoría

#### Versión Original
> Cuando envío un reconocimiento a un compañero, quiero que el sistema se asegure de que el mensaje es el adecuado según el tipo de premio que elegí (como Innovación o Trabajo en equipo). Además, quiero que el sistema sea flexible para que, si en el futuro creamos nuevas categorías de premios, se puedan añadir sus propias reglas sin romper lo que ya funciona

#### Versión Refinada (Generada por Gema)
**Título:** Validación de mensajes de reconocimiento por tipo de categoría

**Descripción:**
**Como** Empleado Sofkiano,
**quiero** que el sistema verifique que mi mensaje de agradecimiento cumple con los requisitos de la categoría seleccionada,
**para** asegurar que el reconocimiento sea relevante, de alta calidad y coherente con el tipo de premio otorgado.

**Criterios de aceptación:**
1. El Producer API debe validar el contenido del mensaje antes de aceptar la solicitud de envío de Kudo.
2. Si el mensaje no cumple con la longitud mínima o los términos requeridos por la categoría seleccionada, el sistema debe retornar un error `400` (Bad Request).
3. El sistema debe permitir la incorporación de nuevas reglas de validación para nuevas categorías sin necesidad de modificar el flujo principal de envío de mensajes.
4. El proceso de validación debe completarse antes de que el mensaje sea publicado en RabbitMQ y antes de emitir la confirmación `202` al usuario.
5. Los mensajes deben ser rechazados si contienen únicamente espacios en blanco o caracteres especiales sin sentido, independientemente de la categoría.

**Notas y preguntas abiertas:**
- El equipo debe definir la matriz de validación por categoría (ej. *Innovación*: mínimo 30 caracteres; *Trabajo en equipo*: debe mencionar un compañero) antes de la sesión de planeación.
- Se debe confirmar si los errores de validación se registrarán en un log de auditoría para analizar patrones de uso fallido.