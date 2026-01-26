Descripción del Proyecto
Este presente proyecto consiste en el desarrollo e implementación de un sistema completo para la gestión de inventario de vehículos. 

Arquitectura y Despliegue en la Nube
El backend y el frontend y la base de datos todo se encuentra desplegado en la plataforma Railway. Esto permite que la aplicación sea accesible desde cualquier ubicación y dispositivo con conexión a internet, eliminando la dependencia de servidores locales para la persistencia de datos.

URL:
`https://autos-api-production-575f.up.railway.app/login`

Interfaz Gráfica de Usuario (Cliente de Escritorio)
Como componente adicional al servicio web, se incluye una aplicación de escritorio que permite interactuar con el sistema de manera visual.

Instrucciones de Ejecución
Para iniciar la interfaz gráfica, siga estos pasos en su entorno de desarrollo (IDE):

1.  Abra el proyecto en su IDE
2.  Manda a correr el servidor local con ´AutosApiApplication.java´
3.  Localice la clase  llamada `LoginFrame.java` y mandale run
4.  Ingresa con credenciales de admin@autos.com , admin123

Especificaciones Técnicas
Para el desarrollo de este sistema se utilizaron las siguientes tecnologías y herramientas:

Lenguaje de Programación: Java (JDK 21)
Framework Backend: Spring Boot 
Gestión de Dependencias: Groovy Gradle
Base de Datos:POSTGRESS (Alojada en Railway)
Persistencia: JPA / Hibernate
Interfaz de Usuario: Java Swing

Documentación de Endpoints
La API expone los siguientes recursos para realizar las operaciones CRUD:

| Método HTTP | Endpoint | Descripción Funcional |

| GET | `/api/autos` | Recupera el listado completo de vehículos registrados. |
| GET | `/api/autos/{id}` | Obtiene los detalles de un vehículo específico mediante su ID. |
| POST | `/api/autos/crear` | Registra un nuevo vehículo en la base de datos. |
| PUT | `/api/autos/{id}` | Actualiza la información de un vehículo existente. |
| DELETE | `/api/autos/{id}` | Elimina el registro de un vehículo del sistema. |

Este proyecto lo hice con fines academicos y si deseas lo puedes descargar y mejorarlo espero les sirve como base.
