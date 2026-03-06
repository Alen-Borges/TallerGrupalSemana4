# Documento de Contexto Empresarial - SofkianOS

**Fecha**: Marzo 3, 2026  
**Estado**: MVP - Sistema Distribuido Funcional  
**Equipo**: Christopher Pallo, Elian Condor, Leonel, Jean Pierre Villacis, Hans Ortiz

---

## 1. Descripción del Proyecto

### Nombre del Proyecto
**SofkianOS - Sistema Distribuido de Kudos**

### Objetivo del Proyecto
SofkianOS transforma la identidad Sofkiana en **Kudos** tangibles. El término Kudos proviene del griego _kŷdos_, significando honor, reconocimiento y prestigio por un logro. El sistema implementa una **Cultura de Recompensas** que celebra contribuciones reales de cada persona, fortaleciendo vínculos entre equipos geográficamente distribuidos.

**Visión**: Sofkian (nuestra esencia) + OS (Sistema Operativo de Kudos) = Cultura de Recompensas

### Propuesta de Valor
- **Reconocimiento Instantáneo**: Los Kudos se envían inmediatamente, sin colas visibles para el usuario
- **Sin Bloqueos**: Arquitectura asíncrona con mensajería. La API responde con 202 y el procesamiento continúa en segundo plano
- **Gamificación Justa**: Categorías con puntos, trazabilidad y un sistema diseñado para escalar con el equipo
- **Procesamiento Masivo**: SofkianOS procesa miles de reconocimientos sin bloqueos gracias al pipeline asíncrono

---

## 2. Flujos de Negocio Críticos

### Flujos Principales

#### Flujo de Envío de Kudos (Principal)
```
Empleado (Frontend)
    ↓
SofkianOS Web (React + Vite)
    ↓ POST /api/v1/kudos
Producer API (Spring Boot)
    ↓ Valida & Publica
RabbitMQ (Topic Exchange)
    ↓
Consumer Worker (Spring Boot Listener)
    ↓ Procesa Gamificación
Base de Datos (PostgreSQL - Futuro)
```

**Características**:
- Response del Producer API: 202 Accepted
- Procesamiento asíncrono en segundo plano
- Sin bloqueos de usuario

#### Flujo de Visualización de Kudos
```
Empleado (Frontend)
    ↓
GET /api/v1/kudos
Producer API (Spring Boot)
    ↓
Base de Datos / Caché
    ↓
JSON Response (200)
    ↓
Frontend Renderiza Listado
```

#### Flujo de Gamificación (Background)
```
RabbitMQ
    ↓
KudosConsumer Component
    ↓ Procesa Lógica
Asigna Puntos / Actualiza Categoría
    ↓
Persistencia en Base de Datos
```

### Módulos/Características Críticos

| Módulo | Responsabilidad | Tecnología |
|--------|-----------------|------------|
| **Frontend (Web SPA)** | UI para envío y visualización de Kudos | React 19.2.0, TypeScript 5.9.3, Vite 7.2.4 |
| **Producer API** | Valida solicitudes y publica eventos | Spring Boot 3.3.5, Java 17, Spring AMQP |
| **Consumer Worker** | Procesa lógica de gamificación | Spring Boot 3.3.5, Java 17, Spring AMQP |
| **Message Broker** | Desacoplamiento y almacenamiento asíncrono | RabbitMQ (Topic Exchange) |
| **Base de Datos** | Persistencia final de kudos y gamificación | PostgreSQL (futuro) |
| **Infraestructura** | Contenedorización y orquestación | Docker, Docker Compose, Terraform/AWS |

---

## 3. Reglas de Negocio y Restricciones

### Reglas de Negocio Clave (RN)

- **RN-001**: Cada Kudo enviado debe ser validado antes de publicarse en RabbitMQ
- **RN-002**: El Producer API responderá con código 202 (Accepted) sin esperar procesamiento
- **RN-003**: El Consumer Worker procesará Kudos de forma asíncrona y escalable
- **RN-004**: Las categorías de Kudos tienen valores de puntos asociados (gamificación)
- **RN-005**: No se permite enviar Kudos a uno mismo (validación pendiente)
- **RN-006**: Cada Kudo enviado es público y visible en el historial del destinatario
- **RN-007**: El sistema debe escalar para procesar miles de Kudos sin bloqueos
- **RN-008**: Los mensajes en RabbitMQ persisten si el Consumer no está disponible

### Restricciones Técnicas

- **RT-001**: RabbitMQ debe estar disponible (local o en contenedor)
- **RT-002**: Producer API escucha en puerto 8082
- **RT-003**: Consumer Worker escucha en puerto 8081
- **RT-004**: Frontend proxya `/api` hacia el backend en desarrollo
- **RT-005**: Timeout de API Client: 10 segundos
- **RT-006**: Java 17 mínimo para servicios backend
- **RT-007**: Node.js LTS requerido para desarrollo frontend

### Consideraciones de Regulación y Cumplimiento

- **Protección de Datos**: No especificada actualmente (pendiente definición)
- **Regulaciones de Industria**: No aplicable hasta el momento
- **Auditoría**: El sistema debe mantener trazabilidad de todos los Kudos enviados
- **GDPR/CCPA**: No aplica en fase MVP (requiere revisión posterior)

---

## 4. Perfiles de Usuario y Roles

### Matriz de Usuarios y Roles

| Rol | Descripción | Estado |
|-----|-------------|--------|
| **Empleado Sofkian** | Usuario que envía y recibe Kudos | ✅ Implementado |
| **Administrador del Sistema** | Gestiona categorías, puntos, límites | 🔄 Futuro |
| **Auditor** | Revisa registro de transacciones | 🔄 Futuro |

### Permisos y Limitaciones

#### Empleado Sofkian (MVP)
**Puede:**
- Enviar Kudos a colegas
- Visualizar lista pública de Kudos
- Ver perfil de reconocimientos (futuro)
- Consultar balance de puntos (futuro)

**No puede:**
- Enviar Kudos a sí mismo (pendiente validación)
- Modificar Kudos ya enviados
- Acceder a datos privados de otros usuarios
- Crear nuevas categorías
- Modificar puntos asignados

#### Administrador (Futuro)
**Puede:**
- Crear y modificar categorías de Kudos
- Definir valores de puntos por categoría
- Establecer límites de envío por período
- Visualizar reportes de gamificación
- Auditar todas las transacciones

**No puede:**
- Eliminar Kudos sin proceso de auditoría
- Modificar Kudos enviados directamente
- Ignorar límites establecidos

---

## 5. Condiciones del Entorno Técnico

### Plataformas Soportadas

| Plataforma | Soporte | Notas |
|------------|---------|-------|
| **Web Desktop** | ✅ Completo | Chrome, Firefox, Safari, Edge |
| **Web Mobile** | ✅ Responsive | Acceso vía navegador |
| **iOS Nativa** | ❌ No | En evaluación |
| **Android Nativa** | ❌ No | En evaluación |

### Stack Tecnológico Detallado

#### Frontend Layer
```
┌─────────────────────────────────────────────┐
│        React Application (SPA)              │
├─────────────────────────────────────────────┤
│ Rendering: React 19.2.0                     │
│ Language: TypeScript 5.9.3                  │
│ Build Tool: Vite 7.2.4                      │
│ Styling: Tailwind CSS 3.4.19                │
│ HTTP Client: Axios 1.13.4                   │
│ Routing: React Router DOM 7.13.0            │
│ Forms: React Hook Form 7.71.1               │
│ Validation: Zod 4.3.6                       │
│ State (Optional): Zustand 5.0.11            │
│ UI Components: PrimeReact 10.9.7            │
│ Runtime (Prod): Nginx Alpine                │
└─────────────────────────────────────────────┘
```

#### Backend Layer - Producer API
```
┌─────────────────────────────────────────────┐
│      Producer API (Spring Boot)             │
├─────────────────────────────────────────────┤
│ Framework: Spring Boot 3.3.5                │
│ Language: Java 17                           │
│ Build Tool: Maven 3.x                       │
│ AMQP: Spring AMQP (Spring Boot)             │
│ Endpoints:                                  │
│   - POST /api/v1/kudos (202 Accepted)      │
│   - GET /health                             │
│ Documentation: Swagger/OpenAPI              │
│ Docker: Official OpenJDK 17 Alpine          │
└─────────────────────────────────────────────┘
```

#### Backend Layer - Consumer Worker
```
┌─────────────────────────────────────────────┐
│    Consumer Worker (Spring Boot)            │
├─────────────────────────────────────────────┤
│ Framework: Spring Boot 3.3.5                │
│ Language: Java 17                           │
│ Build Tool: Maven 3.x                       │
│ AMQP: Spring AMQP (Spring Boot)             │
│ Listener: @RabbitListener                   │
│ Queue: kudos.queue                          │
│ Endpoints:                                  │
│   - GET /api/v1/health                      │
│ Docker: Official OpenJDK 17 Alpine          │
└─────────────────────────────────────────────┘
```

#### Message Broker Layer
```
┌─────────────────────────────────────────────┐
│           RabbitMQ (Message Broker)         │
├─────────────────────────────────────────────┤
│ Broker: RabbitMQ (Última versión)           │
│ Exchange: kudos.exchange (Topic Exchange)   │
│ Queue: kudos.queue                          │
│ Routing Key: kudos.key                      │
│ Persistence: Disk (por defecto)             │
│ Docker Compose: rabbitmq:latest             │
└─────────────────────────────────────────────┘
```

#### Persistence Layer (Futuro)
```
┌─────────────────────────────────────────────┐
│      PostgreSQL Database (Futuro)           │
├─────────────────────────────────────────────┤
│ DBMS: PostgreSQL                            │
│ Tables:                                     │
│   - kudos (id, from_user, to_user, ...)    │
│   - users (id, name, email, ...)           │
│   - categories (id, name, points, ...)      │
│   - audit_log (transaction history)        │
└─────────────────────────────────────────────┘
```

#### Infrastructure Layer
```
┌─────────────────────────────────────────────┐
│         Infrastructure as Code              │
├─────────────────────────────────────────────┤
│ Container Orchestration: Docker Compose     │
│ Cloud Provider: AWS (EC2, RDS futuro)       │
│ IaC Tool: Terraform                         │
│ Image Registry: Docker Hub (futuro)         │
│ CI/CD: Jenkins (ci/Jenkinsfile)             │
└─────────────────────────────────────────────┘
```

### Métodos de Autenticación

- **Actual**: Sistema abierto en MVP (sin autenticación)
- **Futuro**: JWT, OAuth 2.0, LDAP (por definir)

### Proveedores Externos

| Proveedor | Servicio | Estado |
|-----------|----------|--------|
| AWS | Infraestructura Cloud | En evaluación |
| Docker Hub | Registros de Imágenes | Planeado |
| GitHub | Control de Versiones | ✅ Activo |

---

## 6. Casos Especiales o Excepciones

| Escenario | Manejo | Impacto |
|-----------|--------|--------|
| **Worker no disponible** | RabbitMQ persiste mensajes; Consumer procesa al recuperarse | Retardo en gamificación |
| **Validación fallida** | Producer retorna 400 sin publicar a RabbitMQ | Usuario recibe feedback inmediato |
| **Timeout de API** | Configurado en 10 seg; cliente reintenta automáticamente | Experiencia degradada si timeout persiste |
| **Concurrencia de Kudos** | RabbitMQ ordena por mensaje; Consumer escala horizontalmente | SLA < 5 seg por Kudo (estimado) |
| **RabbitMQ no disponible** | Producer falla con 503; Frontend muestra error | Reintento manual del usuario |
| **Crash de Producer API** | Nginx devuelve 502; Usuario puede reintentar | Pérdida de Kudo en tránsito |
| **DB no disponible (futuro)** | Implementar dead-letter queue y reintentos | Kudos no persistidos temporalmente |

---

## 7. Arquitectura (Vistas C1 - C3)

### Vista C1 - Contexto del Sistema

```
┌────────────────────────────────────────────────────────────┐
│                     Sistema SofkianOS                      │
│  (Kultur de Recompensas - Kudos en Tiempo Real)            │
└────────────────────────────────────────────────────────────┘
              ↑                                    ↑
              │                                    │
    ┌─────────┴─────────┐              ┌──────────┴──────────┐
    │                   │              │                     │
 Empleado           RabbitMQ       Base de Datos         Auditor
Sofkiano            (Future)       (PostgreSQL)          (Future)
```

### Vista C2 - Contenedores

```
┌─────────────────────────────────────────────────────────────────────┐
│                        End User (Navegador)                         │
└──────────────────────────────┬──────────────────────────────────────┘
                               │ HTTP/HTTPS
                               ↓
                    ┌──────────────────────┐
                    │   Nginx (Reverse    │
                    │    Proxy - Prod)    │
                    └──────────┬───────────┘
                               │
                ┌──────────────┴──────────────┐
                │                             │
                ↓                             ↓
    ┌──────────────────────┐      ┌──────────────────────┐
    │                      │      │                      │
    │  SofkianOS Frontend  │      │  Producer API        │
    │  (React + Vite)      │      │  (Spring Boot)       │
    │                      │      │                      │
    │ - Kudo Form UI       │      │ - POST /v1/kudos    │
    │ - Kudos List View    │      │ - GET /health       │
    │ - Landing Page       │      │ - Swagger UI        │
    │                      │      │                      │
    └──────────────────────┘      └────────┬─────────────┘
                                           │ AMQP
                                           ↓
                                  ┌────────────────────┐
                                  │                    │
                                  │     RabbitMQ       │
                                  │                    │
                                  │ - Topic Exchange   │
                                  │ - Kudos Queue      │
                                  │ - Persistence      │
                                  │                    │
                                  └────────┬───────────┘
                                           │ AMQP
                                           ↓
                                  ┌────────────────────┐
                                  │                    │
                                  │ Consumer Worker    │
                                  │ (Spring Boot)      │
                                  │                    │
                                  │ - Gamification     │
                                  │ - Points Logic     │
                                  │ - Health Check     │
                                  │                    │
                                  └────────┬───────────┘
                                           │ JDBC
                                           ↓
                                  ┌────────────────────┐
                                  │  PostgreSQL (DB)   │
                                  │  [FUTURO]          │
                                  └────────────────────┘
```

### Vista C3 - Componentes (Producción)

#### Frontend Components
```
SofkianOS Web (React SPA)
├── Navbar Component
├── KudoForm Component
│   ├── From/To Selectors
│   ├── Category Selector
│   ├── Message Textarea
│   └── Submit Button
├── KudosList Component
│   └── KudoItem (List)
├── LandingPage
│   ├── Hero Section
│   ├── About Section
│   ├── How It Works
│   ├── Tech Stack
│   └── Footer
└── Services
    ├── kudosService.ts (API calls)
    └── axiosConfig.ts (HTTP client)
```

#### Backend Components
```
Producer API (Spring Boot)
├── KudosController
│   ├── POST /api/v1/kudos
│   └── GET /health
├── RabbitConfig
│   ├── Queue Declaration
│   ├── Exchange Declaration
│   └── Binding
├── OpenApiConfig
│   └── Swagger Setup
└── HealthController
    └── Health Endpoint

Consumer Worker (Spring Boot)
├── KudosConsumer
│   └── @RabbitListener(kudos.queue)
├── RabbitConfig
│   ├── Queue Declaration
│   ├── Exchange Declaration
│   └── Binding
└── HealthController
    └── Health Endpoint (/api/v1/health)
```

---

## 8. Brechas de Contexto Identificadas

### Críticas (Bloquean MVP)
- [ ] ¿Cuál es el modelo de autenticación definitivo (JWT, OAuth, LDAP)?
- [ ] ¿Cuáles son las categorías iniciales de Kudos y sus valores de puntos?
- [ ] ¿Se permite enviar Kudos a uno mismo o hay restricción?

### Importantes (Afectan producción)
- [ ] ¿Cuál es el límite de Kudos por usuario y período?
- [ ] ¿Existe estrategia de escalabilidad horizontal de Consumer Workers?
- [ ] ¿Cuál es el SLA de procesamiento de mensajes?
- [ ] ¿Se requiere notificación (email/push) al recibir Kudos?
- [ ] ¿Existe estrategia de backup/disaster recovery para RabbitMQ?

### Moderadas (Mejora experiencia)
- [ ] ¿Cómo se manejan disputas o cancelaciones de Kudos procesados?
- [ ] ¿Existe estrategia de borrado/purga de datos históricos?
- [ ] ¿Se requiere funcionalidad de búsqueda/filtro en Kudos?
- [ ] ¿Cuál es el plan de monitoreo y alertas para producción?

### Futuras (Roadmap)
- [ ] ¿Existe plan para Dashboard Administrativo?
- [ ] ¿Se requiere API pública para integraciones terceras?
- [ ] ¿Se planea app móvil nativa?
- [ ] ¿Existe plan de gamificación avanzada (badges, levels)?

---

## 9. Supuestos Actuales (MVP)

| Supuesto | Justificación | Risk |
|----------|---------------|------|
| RabbitMQ siempre disponible | Docker Compose local | ALTO - Requiere health checks |
| Todos los usuarios son Sofkianos | Sin autenticación en MVP | ALTO - Requiere validación de identidad |
| Carga inicial < 1000 Kudos/sec | Proof of concept | MEDIO - Requiere load testing |
| Sin persistencia en PostgreSQL | MVP en memoria | MEDIO - Datos se pierden al reiniciar |
| Sin requerimientos GDPR/CCPA | Fase inicial | MEDIO - Revisar con legal |
| Single Consumer Worker instance | Simplificar MVP | BAJO - Escalar horizontalmente en prod |

---

## 10. Preguntas para Stakeholder / Product Owner

### Bloqueos MVP
1. ¿Cuál es el modelo de autenticación para usuarios finales?
JWT
2. ¿Cuáles son las categorías iniciales y tabla de puntos?
CATEGORIAS QUEMADAS DEBIDO AL TIEMPO
3. ¿Se permite enviar Kudos a uno mismo?
NO

### Producción
4. ¿Cuál es el límite de Kudos por usuario/período?
Actualmente no hay
5. ¿Cuál es el SLA esperado para procesamiento?
6. ¿Se requieren notificaciones al recibir Kudos?
Si
7. ¿Cuál es la estrategia de escalabilidad?
Con kubernetes
8. ¿Existe plan de backup/DR para RabbitMQ?
DLQ
9. ¿Cuál es el plan de monitoreo y alertas?
Mediante GRAFANA/PROMETEUS

### Futuro
10. ¿Cuándo se implementa Dashboard Administrativo?
cUANTO SE IMOKMENTE AUTH/AUTORIZACION
11. ¿Se planea API pública para integraciones?
NO
12. ¿Timeline para app móvil nativa?
DE MOMEENTO
13. ¿Plan de gamificación avanzada (badges)?

---

## 11. Documentación de Referencia

### Documentos Internos
- **README.md**: Descripción general del proyecto
- **frontend/README.md**: Detalles técnicos del frontend
- **producer-api/README.md**: Especificación Producer API
- **consumer-worker/README.md**: Especificación Consumer Worker
- **frontend/ARCHITECTURE.md**: Arquitectura React detallada
- **Documentación Official**: https://sofkianos-docs.vercel.app/
- **Tracking del Proyecto**: [Google Sheets](https://docs.google.com/spreadsheets/d/1B9BNs2P8Uc9wHLLaO9lWZXL5TQA7hflkfwGPXLah0is/)

### Documentos de Calidad
- **TEST_PLAN.md**: Plan de pruebas y QA
- **TDD.md**: Enfoque de desarrollo test-driven
- **DEUDA_TECNICA.md**: Análisis de deuda técnica

### Documentos de Infraestructura
- **aws/**: Configuración Terraform para AWS
- **Docker/**: Configuraciones Docker para entornos
- **docker-compose.yml**: Orquestación local

---

## 12. Métricas de Éxito y KPIs

### MVP - Funcionalidad
- ✅ Usuarios pueden enviar Kudos (202 response)
- ✅ Kudos visible en tiempo real en listado
- ✅ Sistema procesa 100+ Kudos/segundo sin bloqueos
- ✅ Tasa de error < 1%

### Producción - Performance
- [ ] Latencia API < 500ms (p95)
- [ ] Procesamiento Consumer < 5 seg (p95)
- [ ] Disponibilidad > 99.9%
- [ ] Error rate < 0.1%

### Negocio
- [ ] Adopción: 80%+ empleados activos
- [ ] NPS: > 8
- [ ] Cultura de reconocimiento fortalecida

---

**Fin del Documento de Contexto Empresarial**

---

*Documento creado: Marzo 3, 2026*  
*Próxima revisión: Marzo 2026 (Monthly)*
