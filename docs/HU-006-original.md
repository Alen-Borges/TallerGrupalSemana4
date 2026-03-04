## US-006 Dockerización Multi-Stage del Frontend

### 1. Definición de la HU

**Como** persona de operaciones
**Quiero** dockerizar la aplicación frontend utilizando un build multi-stage
**Para** compilar el código fuente aislado y servir estáticos de forma ultraligera en producción.

### 2. Especificaciones de Arquitectura y Despliegue

* **Capa de Arquitectura:** Infraestructura (Contenedores)
* **Herramientas:** Docker, Node.js 20 (Alpine), Nginx (Alpine).
* **Estrategia de Despliegue:** Multi-Stage Build estático.

### 3. Matriz de Calidad INVEST

| Criterio | Puntuación (0-3) | Justificación de la nota |
| :--- | :---: | :--- |
| **Independent** | 3 | Completamente autocontenido, la compilación de la imagen no exige correr dependencias. |
| **Negotiable** | 2 | Posibilidad de cambiar parámetros estáticos del Nginx, pero se sigue un patrón rígido de frontend. |
| **Valuable** | 3 | Reduce radicalmente el peso y la superficie de vulnerabilidad eliminando el runtime de Node en prod. |
| **Estimable** | 3 | Acotado a un `Dockerfile` de 14 líneas de rápida ejecución. |
| **Small** | 3 | Solo incluye la descarga de dependencias, transpilación y servidor Nginx. |
| **Testable** | 3 | La imagen se puede instanciar localmente testeando el puerto resultante 5173. |

### 4. Validación (Gherkin)

* **Escenario:** Construcción de imagen mínima usando Nginx Server
  * **Dado** el código fuente React sin compilar en el directorio `frontend/`
  * **Cuando** se ejecuta `docker build` en el entorno automatizado
  * **Entonces** la etapa `builder` transfiere la carpeta `dist` al entorno de `nginx:alpine`, configurando Nginx y arrancando con modo daemon offline.

### 5. Definición de Hecho (DoD)

* [x] Instalación de dependencias realizada como capa independiente en etapa de constructora.
* [x] Imagen final carente de runtime de Node y código fuente base.
* [x] Configuración embebida vía `nginx.conf`.
* [x] Exposición del puerto 5173 funcional mediante script inicial predefinido.