# 🚀 API REST Demo (Enfoque Code First)

Este proyecto es parte de una demostración técnica que compara la arquitectura REST (Code First) frente a SOAP (API First).

## 📌 Características
- **Código primero (Code First)**: El desarrollo comenzó estructurando las clases Java y los controladores en Spring. El contrato de la API y las rutas son inherentes al código mismo, lo cual permite una creación rápida y ágil.
- **Formato Ligero**: Todas las respuestas se emiten en formato `JSON`, lo que reduce el uso de ancho de banda y lo hace ideal para aplicaciones web y móviles modernas.
- **Protocolo de Estados de Transferencia (REST)**: Manipulamos la entidad `Usuario` mediante recursos predecibles, en este caso usando el verbo HTTP `GET`.
- **Manejo de Respuestas Nativas**: Los errores y estados satisfactorios están enlazados directamente a los *Status Codes* nativos del protocolo HTTP (`200 OK`, `404 Not Found`).

---

## 🏗️ Requisitos Tecnológicos
- **Java 17**
- **Spring Boot 3.x**
- **Maven** o **Docker**

## 💻 Cómo Ejecutarlo

Puedes levantar el servicio directamente si tienes Maven y Java instalados:
```bash
mvn clean spring-boot:run
```

O puedes usar Docker para inicializar el contenedor que ya viene preconfigurado con todo lo necesario en la raíz de este directorio:
```bash
docker compose up --build -d
```

---

## 🌐 Endpoints Disponibles

La aplicación funciona por defecto en el puerto local **8080**.

### 1. Obtener un Usuario Existente (Simulación Exitosa)
*   **Método:** `GET`
*   **URL:** `http://localhost:8080/usuarios/1`
*   **Response Esperada (HTTP 200 OK):**
```json
{
    "id": 1,
    "nombre": "Juan Pérez",
    "email": "juan.perez@example.com"
}
```

### 2. Obtener un Usuario Inexistente (Simulación Error)
*   **Método:** `GET`
*   **URL:** `http://localhost:8080/usuarios/99`
*   **Response Esperada (HTTP 404 NOT FOUND):**
No devolverá un cuerpo de texto, sino un claro estado 404 en la cabecera del cliente, dictaminando que el recurso de ese "ID" no fue localizado.
