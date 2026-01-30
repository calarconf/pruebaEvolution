# Sistema de Gestión de Biblioteca - Documentación Técnica Completa

  

## Índice

  


1. [Parte 1: Modelado y Diseño de Base de Datos](#parte-1-modelado-y-diseño-de-base-de-datos)

2. [Parte 2: Creación de API REST](#parte-2-creación-de-api-rest)

3. [Parte 3: Aplicación Móvil Android](#parte-3-aplicación-móvil-android)

  


  

---

  

## Parte 1: Modelado y Diseño de Base de Datos

  

### 1.1 Contexto del Problema

  

Se requiere diseñar un sistema para gestionar una biblioteca con los siguientes requisitos:

  

- Cada libro tiene un título, un autor asociado, una categoría y una cantidad disponible

- Un autor tiene un nombre y una lista de libros escritos

- Un usuario tiene un nombre, un correo electrónico y una lista de libros prestados

  

### 1.2 Modelo de Datos Relacional

  

#### Estructura de Tablas

  

**Tabla: autores**

```sql

- id_autor (SERIAL, PRIMARY KEY)

- nombre (VARCHAR(255), NOT NULL)

```

  

**Tabla: libros**

```sql

- id_libro (SERIAL, PRIMARY KEY)

- titulo (VARCHAR(255), NOT NULL)

- id_autor (INT, FOREIGN KEY → autores.id_autor)

- categoria (VARCHAR(100), NOT NULL)

- cantidad_disponible (INT, NOT NULL, DEFAULT 0)

```

  

**Tabla: usuarios**

```sql

- id_usuario (SERIAL, PRIMARY KEY)

- nombre (VARCHAR(255), NOT NULL)

- correo_electronico (VARCHAR(255), NOT NULL, UNIQUE)

```

  

**Tabla: prestamos**

```sql

- id_prestamo (SERIAL, PRIMARY KEY)

- id_usuario (INT, FOREIGN KEY → usuarios.id_usuario)

- id_libro (INT, FOREIGN KEY → libros.id_libro)

- fecha_prestamo (DATE, NOT NULL, DEFAULT CURRENT_DATE)

- fecha_devolucion (DATE)

```

  

#### Relaciones entre Tablas

  

- **Autores ← Libros**: Relación uno a muchos (un autor puede escribir múltiples libros)

- **Usuarios ← Prestamos**: Relación uno a muchos (un usuario puede tener múltiples préstamos)

- **Libros ← Prestamos**: Relación uno a muchos (un libro puede ser prestado múltiples veces)

  

#### Diagrama Relacional

  
  ![[Call Center SAC Ecosystem-2026-01-29-232514.png]]
  

### 1.3 Implementación en PostgreSQL

  

#### Script de Creación

  

```sql

-- Tabla Autores

CREATE TABLE autores (

    id_autor SERIAL PRIMARY KEY,

    nombre VARCHAR(255) NOT NULL

);

  

-- Tabla Libros

CREATE TABLE libros (

    id_libro SERIAL PRIMARY KEY,

    titulo VARCHAR(255) NOT NULL,

    id_autor INT NOT NULL,

    categoria VARCHAR(100) NOT NULL,

    cantidad_disponible INT NOT NULL DEFAULT 0,

    FOREIGN KEY (id_autor) REFERENCES autores(id_autor) ON DELETE CASCADE

);

  

-- Tabla Usuarios

CREATE TABLE usuarios (

    id_usuario SERIAL PRIMARY KEY,

    nombre VARCHAR(255) NOT NULL,

    correo_electronico VARCHAR(255) NOT NULL UNIQUE

);

  

-- Tabla Prestamos

CREATE TABLE prestamos (

    id_prestamo SERIAL PRIMARY KEY,

    id_usuario INT NOT NULL,

    id_libro INT NOT NULL,

    fecha_prestamo DATE NOT NULL DEFAULT CURRENT_DATE,

    fecha_devolucion DATE,

    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,

    FOREIGN KEY (id_libro) REFERENCES libros(id_libro) ON DELETE CASCADE

);

```

  

### 1.4 Consultas SQL

  

#### Consulta Principal: Libros por Autor

  

Consulta que devuelve todos los libros de un autor específico, incluyendo título y categoría:

  

```sql

SELECT

    l.titulo,

    l.categoria

FROM

    libros l

INNER JOIN

    autores a ON l.id_autor = a.id_autor

WHERE

    a.nombre = 'Gabriel García Márquez';

```

  

### 1.5 Optimización de Consultas con Índices

  

La consulta original funcionaba correctamente pero podía ser lenta con muchos datos. El problema principal era que la base de datos debía revisar todas las filas de las tablas para encontrar los resultados.

  

#### Por qué usar índices

  
Los índices funcionan como el índice de un libro: en lugar de leer todo el libro página por página, el índice dice exactamente dónde está la información que uno busca. Sin índices, la base de datos hace un "escaneo completo" revisando cada fila una por una. Con índices, puede saltar directamente a los datos relevantes.


#### Índices implementados

  

Se crearon tres índices principales:

  

**1. Índice en el nombre del autor:**

```sql

CREATE INDEX idx_autores_nombre ON autores(nombre);

```

Permite buscar autores por nombre rápidamente.

  

**2. Índice compuesto en libros:**

```sql

CREATE INDEX idx_libros_covering ON libros(id_autor, titulo, categoria);

```

Contiene todas las columnas que necesita la consulta, permitiendo que la base de datos responda sin acceder a la tabla completa.

  

**3. Índices adicionales para préstamos:**

```sql

CREATE INDEX idx_prestamos_usuario ON prestamos(id_usuario);

CREATE INDEX idx_prestamos_libro ON prestamos(id_libro);

```

Aceleran las búsquedas de préstamos por usuario o por libro.

  
  

### 1.6 Índices Implementados

  

```sql

CREATE INDEX idx_autores_nombre ON autores(nombre);

CREATE INDEX idx_libros_covering ON libros(id_autor, titulo, categoria);

CREATE INDEX idx_prestamos_usuario ON prestamos(id_usuario);

CREATE INDEX idx_prestamos_libro ON prestamos(id_libro);

```

  

---

  

## Parte 2: Creación de API REST

  

### 2.1 Tecnologías Utilizadas

  

- Java 17

- Spring Boot 3.2.1

- Spring Data JPA

- PostgreSQL

- Maven

  

### 2.2 Estructura del Proyecto

  

El proyecto se organizó en capas para separar responsabilidades:

  

- **Controller**: Maneja las peticiones HTTP y define los endpoints

- **Service**: Contiene la lógica de negocio

- **Repository**: Se encarga del acceso a la base de datos

- **Model**: Define las entidades que se mapean a las tablas

- **DTO**: Objetos para transferir datos entre capas

- **Exception**: Manejo centralizado de errores

  

### 2.3 Configuración

  

**Archivo application.properties:**

```properties

spring.datasource.url=jdbc:postgresql://localhost:5432/biblioteca_db

spring.datasource.username=postgres

spring.datasource.password=tu_password

spring.jpa.hibernate.ddl-auto=none

```

  

### 2.4 Modelos (Entidades)

  

Se crearon cuatro entidades principales que representan las tablas de la base de datos:

  

- **Autor**: Con su ID y nombre

- **Libro**: Con título, autor, categoría y cantidad disponible

- **Usuario**: Con nombre y correo electrónico

- **Prestamo**: Relaciona usuarios con libros prestados

  

Las entidades usan anotaciones JPA como `@Entity`, `@Table`, `@Id`, `@ManyToOne` y `@OneToMany` para mapear las relaciones con la base de datos.

  

### 2.5 Repositorios

  

Se utilizó Spring Data JPA para crear los repositorios. Simplemente extendiendo `JpaRepository`, Spring proporciona automáticamente métodos para:

  

- Guardar datos

- Buscar por ID

- Listar todos los registros

- Eliminar registros

  

También se agregaron métodos personalizados como buscar libros por autor o categoría.

  

### 2.6 Servicios

  

Los servicios contienen la lógica de negocio. Por ejemplo, al crear un libro se valida que el autor exista antes de guardarlo. También se encarga de convertir entre entidades y DTOs.

  

### 2.7 Controladores (API REST)

  

El controlador define los endpoints que puede usar un cliente:

  

**Endpoints principales implementados:**

  

- `GET /api/libros` - Listar todos los libros

- `POST /api/libros` - Crear un nuevo libro

- `GET /api/libros/{id}` - Obtener un libro específico

- `PUT /api/libros/{id}` - Actualizar un libro

- `DELETE /api/libros/{id}` - Eliminar un libro

  

### 2.8 Manejo de Errores

  

Se implementó un manejador global de excepciones que intercepta todos los errores y devuelve respuestas JSON consistentes con:

  

- Timestamp

- Código de estado HTTP

- Mensaje de error

- Ruta donde ocurrió el error

  

### 2.9 Ejemplo de Uso

  

**Listar libros:**

```http

GET http://localhost:8080/api/libros

```

  

**Crear libro:**

```http

POST http://localhost:8080/api/libros

Content-Type: application/json

  

{

  "titulo": "Nuevo Libro",

  "idAutor": 1,

  "categoria": "Ficción",

  "cantidadDisponible": 5

}

```

  

**Eliminar un Libro**

  

#### Request

```http

DELETE http://localhost:8080/api/libros/1

```

  

#### Ejemplo con cURL

```bash

curl -X DELETE http://localhost:8080/api/libros/11

```
  
**Actualizar un Libro**

  

#### Request

```http

PUT http://localhost:8080/api/libros/1

Content-Type: application/json

```

  

#### Request Body

```json

{

  "titulo": "Cien años de soledad (Edición Especial)",

  "idAutor": 1,

  "categoria": "Ficción",

  "cantidadDisponible": 10

}

```
---

  

## Parte 3: Aplicación Móvil Android

  

### 3.1 Tecnologías Utilizadas

  

- Kotlin

- Jetpack Compose (para la interfaz)

- Retrofit (para consumir la API)

- Gson (para convertir JSON a objetos)

- Coroutines (para operaciones asíncronas)

  

### 3.2 Estructura

  

La aplicación se organizó en:


- **model**: Clases de datos (Libro, Autor)

- **api**: Cliente HTTP con Retrofit

- **MainActivity**: Interfaz de usuario


### 3.3 Configuración
 

**Permisos en AndroidManifest.xml:**

```xml

<uses-permission android:name="android.permission.INTERNET" />

```

  

**Dependencias principales:**

- Retrofit 2.9.0 para consumir la API REST

- Gson para convertir JSON

- Coroutines para programación asíncrona

  

### 3.4 Cliente de API

  

Se configuró Retrofit para conectarse a la API. La URL usada es `http://10.0.2.2:8080/` porque:

  

- `10.0.2.2` es la IP que usa el emulador Android para acceder a `localhost` de la computadora

- Si fuera un dispositivo físico, se usaría la IP real de la PC en la red local

  

### 3.5 Interfaz de Usuario

  

La aplicación muestra:

  

- Un botón para recargar los libros

- Una lista con todos los libros obtenidos de la API

- Cada libro se muestra en una tarjeta con:

  - Título

  - Nombre del autor

  - Categoría

  - Cantidad disponible

Prueba:
![[Pasted image 20260129190059.png]]

  

### 3.6 Funcionamiento

  

Cuando la aplicación inicia:

  

1. Se hace una petición HTTP GET a la API

2. Retrofit convierte el JSON recibido en una lista de objetos Libro

3. La interfaz se actualiza automáticamente mostrando los libros

4. Si hay un error, se muestra un mensaje informativo

  

### 3.7 Manejo de Estados

  

La aplicación maneja tres estados principales:

  

- **Cargando**: Muestra un indicador de progreso

- **Éxito**: Muestra la lista de libros

- **Error**: Muestra un mensaje describiendo el problema

  

### 3.8 Comunicación con la API

  

La aplicación usa Kotlin Coroutines para hacer las peticiones de forma asíncrona, evitando que la interfaz se congele mientras espera la respuesta del servidor.