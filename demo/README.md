# 🚀 DEMO TÉCNICA: REST vs SOAP

¡Bienvenido al ecosistema demostrativo del Taller Grupal de la API! Este repositorio concentra una dualidad directa para la presentación que busca comprobar, visualizar y experimentar palpablemente las grandes diferencias arquitectónicas entre construir un Web Service utilizando un enfoque moderno y ligero (REST / Code First) frente al enfoque empresarial pesado gobernado por contratos rígidos (SOAP / API First). 

---

## 🗂️ Estructura del Proyecto

Esta carpeta madre contiene la orquestación general de la Demo. Cuenta con dos subproyectos Java/Spring Boot completamente separados con sus propios puertos de entrada y lógicas enlazadas a un mismo contenedor Docker de administración.

- `/demo-rest`: El proyecto demostrativo para el enfoque estandarizado en estado, objetos en formato intercambiable JSON y la filosofía popular **Code First**.
- `/demo-soap`: El proyecto construido encima del XML nativo, validación mediante WSDL, control profundo de metadatas encapsuladas en envelopes (SOAP) y el uso exigido del paradigma de desarrollo por contratos **API First**.
- `docker-compose.yml`: Controlador global que orquesta paralelamente las infraestructuras de ambos repositorios dentro de la red temporal de Docker, facilitando la prueba de concepto para la Demo.

## 🛠️ Despliegue Paralelo (Docker)
Recomendamos el uso de la *containerización* de Docker para garantizar la estabilidad de esta exposición. Si logras ejecutar el siguiente comando en esta raíz, tanto la API REST (Puerto `8080`) como la API SOAP (Puerto `8081`) estarán activas en un par de segundos, incluyendo la construcción *under-the-hood* de Maven en las capas constructivas para levantar el código a sus estadios funcionales finales sin dependencias explícitas en tu máquina.

```bash
docker compose up --build -d
```
> ***NOTA:*** Para culminarlas usa `docker compose down`.


## 📖 Guías Rápidas por Proyecto

Cada variante cuenta con sus endpoints detallados, configuraciones, flujos de fallos esperados y la demostración de por qué fueron construidos en dicha orientación. 

Dirígete de inmediato a los documentos instructivos individuales para desentrañar lo que contiene el interior de sus arquitecturas:
* [Léeme de REST (Code First)](./demo-rest/README.md)
* [Léeme de SOAP (Api First)](./demo-soap/README.md)


---

## 📊 Tabla Comparativa Resumen para la Presentación


| Criterio                     | API REST (Code First)                                                                 | API SOAP (API First)                                                                              |
|------------------------------|---------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|
| **Formato predominante**     | **JSON** (Texto plano simplificado, excelente legibilidad)                            | **XML** (Basado en envolturas, jerarquía e InfoSet)                                               |
| **Peso en Tráfico**           | **Ligero** (~70 bytes para esta demo)                                                 | **Pesado** (~350+ bytes debido al Envelope SOAP + namespaces)                                     |
| **Complejidad Estructural**  | **Baja**. Respuesta plana.                                                            | **Alta**. Requiere SOAP Envelope, Header, y etiquetas en el Body.                                 |
| **Manejo de Errores**        | **Protocol-level** (Uso activo de los códigos HTTP: `404`, `500`).                    | **Payload-level** (Error incrustado en el cuerpo dentro del tag `<SOAP:Fault>`).                  |
| **Formalidad Contractual**   | **Opcional / A posteriori** (Swagger de inferencia sobre el código final)             | **Estricta / A priori** (Validación WSDL/XSD impuesta al compilar).                               |
| **Curva / Agilidad**          | **Muy Ágil**. Perfecto para startups y cambios iterativos veloces.                    | **Baja Agilidad**. Setup pesado, integración a plugins (JAXB). Útil para entes conservadores B2B. |

*Ejemplificación demostrativa concebida para la demostración del Taller Grupal Semana 4.*
