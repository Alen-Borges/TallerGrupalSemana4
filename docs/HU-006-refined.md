# US-006 — Dockerización multi-stage de frontend React para despliegue ligero con Nginx

---

## Descripción

**Como** persona de operaciones,
**quiero** dockerizar la aplicación frontend utilizando un build multi-stage,
**para** compilar el código fuente aislado y servir estáticos de forma ultraligera en producción.

---

## Criterios de Aceptación

**CA-01 — Construcción de imagen**

- **Dado que** el código fuente React 19.2.0 se encuentra en el entorno de construcción Node.js 20 Alpine,
- **Cuando** se ejecuta el comando de build de Vite 7.2.4,
- **Entonces** el proceso debe generar el directorio `/dist/` conteniendo el archivo `index.html` y los activos minificados.

---

**CA-02 — Etapa de producción**

- **Dado que** los archivos estáticos han sido generados en la etapa de builder,
- **Cuando** se transfieren a la imagen base de Nginx Alpine,
- **Entonces** los archivos deben estar alojados en `/usr/share/nginx/html/` y el binario de Node.js no debe estar presente en el PATH del sistema.

---

**CA-03 — Seguridad y limpieza de imagen**

- **Dado que** la imagen de producción ha sido creada,
- **Cuando** se inspecciona el contenido de la imagen final,
- **Entonces** no deben existir archivos con extensión `.ts`, `.tsx` ni la carpeta `node_modules`, manteniendo un tamaño total de imagen inferior a 50MB.

---

**CA-04 — Configuración SPA / enrutamiento**

- **Dado que** la aplicación es una Single Page Application (SPA),
- **Cuando** se solicita una ruta no existente físicamente en el servidor,
- **Entonces** Nginx debe redirigir la petición al archivo `index.html` devolviendo un código de estado HTTP 200.

---

**CA-05 — Validación del servicio**

- **Dado que** el contenedor se encuentra en ejecución escuchando en el puerto 80,
- **Cuando** se realiza una petición HTTP GET a la dirección del host,
- **Entonces** el servidor debe responder con un código de estado HTTP 200 y el encabezado de respuesta debe identificar el servidor como Nginx.

---

## Definición de Hecho (DoD)

- [ ] Instalación de dependencias realizada como capa independiente en etapa constructora.
- [ ] Imagen final carente de runtime de Node y código fuente base.
- [ ] Configuración embebida vía `nginx.conf`.
- [ ] Exposición del puerto **80** funcional mediante script inicial predefinido.

---

## Notas Técnicas

- Uso de Multi-Stage Build estático.
- Node.js 20 (Alpine) y Nginx (Alpine) como imágenes base obligatorias.
- Versiones de referencia: React 19.2.0 / Vite 7.2.4 (no deben hardcodearse en los CAs ante actualizaciones).
- **RT-007:** Node.js LTS requerido para desarrollo.
- **RT-004:** El frontend proxya `/api` hacia el backend únicamente en entorno de desarrollo; no aplica en la imagen de producción.