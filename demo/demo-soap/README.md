# 🚀 API SOAP Demo (Enfoque API First)

Este proyecto es la contraparte en la demostración técnica, utilizando la arquitectura SOAP, bajo un estricto enfoque basado en contratos (API First).

## 📌 Características
- **Contrato Estricto Primero (API First)**: En lugar de crear código Java, el ciclo de vida empezó diseñando un archivo XML (`usuarios.xsd`). A partir de este contrato neutral, el compilador autogenera jerarquías de clases y modelos en Java.
- **Formato Formal**: Utiliza SOAP Envelopes en `XML` tanto para las peticiones como para las respuestas. Es pesado, pero provee un control increíble para integraciones B2B complejas, auditorías y transacciones bancarias.
- **RPC (Remote Procedure Calls)**: En oposición a REST, aquí enviamos una orden concreta (ej. "getUsuarioRequest") encapsulada de forma opaca sobre una sola ruta global (usualmente a través de POST).
- **Control Centralizado de Errores**: Si ocurre un error, en vez de delegar las explicaciones a los códigos HTTP, SOAP provee en su misma carga (payload XML) una estructura designada como `<SOAP-ENV:Fault>`, donde se describe en profundidad el fallo acaecido a nivel cliente o servicio.

---

## 🏗️ Requisitos Tecnológicos
- **Java 17**
- **Spring Boot 3.x**
- **Maven plugin `jaxb2`** (Para generar las clases del XSD en el ciclo Compile).
- **Docker** (Opcional)

## 💻 Cómo Ejecutarlo

Si usarás Maven localmente, primero es *obligatorio* que compiles para que las definiciones XSD construyan los modelos en memoria:
```bash
mvn clean compile
mvn spring-boot:run
```

Si usas Docker, el Dockerfile ya se encarga de este ciclo automáticamente:
```bash
docker compose up --build -d
```

---

## 🌐 Endpoints Disponibles

La aplicación funciona por defecto en el puerto local **8081**. A diferencia de REST, hay un único endpoint global que atiende peticiones. 

El archivo WSDL que expone y certifica todas las capacidades del servicio lo puedes descargar en: `http://localhost:8081/ws/usuarios.wsdl`.

### 1. Petición SOAP: Búsqueda Exitosa
*   **Método:** `POST`
*   **URL:** `http://localhost:8081/ws`
*   **Header Obligatorio:** `Content-Type: text/xml`
*   **Body Request:**
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:usu="http://demo.com/soap/usuarios">
   <soapenv:Header/>
   <soapenv:Body>
      <usu:getUsuarioRequest>
         <usu:id>1</usu:id>
      </usu:getUsuarioRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

*   **Response Esperada:**
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
    <SOAP-ENV:Header/>
    <SOAP-ENV:Body>
        <ns2:getUsuarioResponse xmlns:ns2="http://demo.com/soap/usuarios">
            <ns2:usuario>
                <ns2:id>1</ns2:id>
                <ns2:nombre>Juan Pérez</ns2:nombre>
                <ns2:email>juan.perez@example.com</ns2:email>
            </ns2:usuario>
        </ns2:getUsuarioResponse>
    </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

### 2. Petición SOAP: Error (SOAP Fault)
*   **Método:** `POST`
*   **URL:** `http://localhost:8081/ws`
*   **Header Obligatorio:** `Content-Type: text/xml`
*   **Body Request:** Igual al anterior, pero usando el ID con valor 99 (`<usu:id>99</usu:id>`).
*   **Response Esperada (SOAP Fault):**
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
    <SOAP-ENV:Body>
        <SOAP-ENV:Fault>
            <faultcode>SOAP-ENV:Client</faultcode>
            <faultstring xml:lang="en">Usuario no encontrado con ID: 99</faultstring>
        </SOAP-ENV:Fault>
    </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```
