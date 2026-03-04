# HU - 006 Refined

# Optimización y contenedorización del frontend de SofkianOS para despliegue productivo

## Descripción

**Como** desarrollador del equipo de infraestructura,
**quiero** automatizar la creación de una imagen Docker liviana mediante una construcción multi-etapa,
**para** reducir los tiempos de despliegue, minimizar la superficie de ataque y asegurar que el frontend se sirva eficientemente mediante Nginx.

---

## Criterios de aceptación

- El `Dockerfile` debe utilizar una etapa de construcción inicial basada en **Node JS LTS** para compilar la aplicación React y transformar el código TypeScript.
- La etapa final de ejecución debe utilizar una imagen de **Nginx Alpine** para servir únicamente los archivos estáticos generados en el build.
- El contenedor resultante no debe incluir el código fuente, dependencias de desarrollo ni herramientas del sistema operativo que no sean estrictamente necesarias.
- Se debe incluir un archivo `.dockerignore` para evitar que la carpeta `node_modules` local y otros archivos de configuración innecesarios se copien al contexto de construcción.
- La configuración de Nginx dentro del contenedor debe permitir la recarga de rutas directas para soportar el funcionamiento de **React Router** sin errores de archivo no encontrado.
- La imagen debe integrarse correctamente en el archivo `docker-compose` del proyecto y responder en el puerto configurado sin bloqueos.

---

## Notas y preguntas abiertas

- El equipo debe decidir si el escaneo de vulnerabilidades de la imagen se realizará dentro del `Dockerfile` o como un paso independiente en el pipeline de Jenkins.
- Queda pendiente definir si se usará una imagen espejo interna para las dependencias de Node por motivos de seguridad corporativa.