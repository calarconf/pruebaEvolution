# Diseño de API REST - Sistema de Gestión de Biblioteca

## 📋 Descripción General
API RESTful para gestionar libros, autores, usuarios y préstamos de una biblioteca.

## 🔗 Base URL
```
http://localhost:8080/api
```

## 📚 Endpoints - LIBROS

### 1. Listar Todos los Libros
```http
GET /api/libros
```

**Response 200 OK:**
```json
[
  {
    "idLibro": 1,
    "titulo": "Cien años de soledad",
    "autor": {
      "idAutor": 1,
      "nombre": "Gabriel García Márquez"
    },
    "categoria": "Ficción",
    "cantidadDisponible": 5
  }
]
```

### 2. Obtener Libro por ID
```http
GET /api/libros/{id}
```

**Response 200 OK:**
```json
{
  "idLibro": 1,
  "titulo": "Cien años de soledad",
  "autor": {
    "idAutor": 1,
    "nombre": "Gabriel García Márquez"
  },
  "categoria": "Ficción",
  "cantidadDisponible": 5
}
```

**Response 404 Not Found:**
```json
{
  "timestamp": "2026-01-29T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Libro no encontrado con ID: 99"
}
```

### 3. Crear Nuevo Libro
```http
POST /api/libros
Content-Type: application/json
```

**Request Body:**
```json
{
  "titulo": "El amor en los tiempos del cólera",
  "idAutor": 1,
  "categoria": "Romance",
  "cantidadDisponible": 3
}
```

**Response 201 Created:**
```json
{
  "idLibro": 11,
  "titulo": "El amor en los tiempos del cólera",
  "autor": {
    "idAutor": 1,
    "nombre": "Gabriel García Márquez"
  },
  "categoria": "Romance",
  "cantidadDisponible": 3
}
```

**Response 400 Bad Request:**
```json
{
  "timestamp": "2026-01-29T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El título es obligatorio"
}
```

### 4. Actualizar Libro
```http
PUT /api/libros/{id}
Content-Type: application/json
```

**Request Body:**
```json
{
  "titulo": "Cien años de soledad (Edición especial)",
  "idAutor": 1,
  "categoria": "Ficción",
  "cantidadDisponible": 10
}
```

**Response 200 OK:**
```json
{
  "idLibro": 1,
  "titulo": "Cien años de soledad (Edición especial)",
  "autor": {
    "idAutor": 1,
    "nombre": "Gabriel García Márquez"
  },
  "categoria": "Ficción",
  "cantidadDisponible": 10
}
```

### 5. Eliminar Libro
```http
DELETE /api/libros/{id}
```

**Response 204 No Content**

### 6. Buscar Libros por Autor
```http
GET /api/libros/autor/{nombreAutor}
```

**Response 200 OK:**
```json
[
  {
    "idLibro": 1,
    "titulo": "Cien años de soledad",
    "autor": {
      "idAutor": 1,
      "nombre": "Gabriel García Márquez"
    },
    "categoria": "Ficción",
    "cantidadDisponible": 5
  }
]
```

### 7. Buscar Libros por Categoría
```http
GET /api/libros/categoria/{categoria}
```

**Response 200 OK:**
```json
[
  {
    "idLibro": 1,
    "titulo": "Cien años de soledad",
    "autor": {
      "idAutor": 1,
      "nombre": "Gabriel García Márquez"
    },
    "categoria": "Ficción",
    "cantidadDisponible": 5
  }
]
```

## 👤 Endpoints - AUTORES

### 1. Listar Todos los Autores
```http
GET /api/autores
```

### 2. Crear Autor
```http
POST /api/autores
Content-Type: application/json
```

**Request Body:**
```json
{
  "nombre": "Pablo Neruda"
}
```

## 👥 Endpoints - USUARIOS

### 1. Listar Usuarios
```http
GET /api/usuarios
```

### 2. Crear Usuario
```http
POST /api/usuarios
Content-Type: application/json
```

**Request Body:**
```json
{
  "nombre": "Pedro López",
  "correoElectronico": "pedro.lopez@email.com"
}
```

## 📖 Endpoints - PRÉSTAMOS

### 1. Listar Préstamos
```http
GET /api/prestamos
```

### 2. Crear Préstamo
```http
POST /api/prestamos
Content-Type: application/json
```

**Request Body:**
```json
{
  "idUsuario": 1,
  "idLibro": 3,
  "fechaPrestamo": "2026-01-29"
}
```

### 3. Devolver Libro (Actualizar Préstamo)
```http
PUT /api/prestamos/{id}/devolver
```

**Response 200 OK:**
```json
{
  "idPrestamo": 5,
  "usuario": {
    "idUsuario": 1,
    "nombre": "Juan Pérez"
  },
  "libro": {
    "idLibro": 3,
    "titulo": "Crónica de una muerte anunciada"
  },
  "fechaPrestamo": "2026-01-29",
  "fechaDevolucion": "2026-01-29"
}
```

## ⚠️ Códigos de Estado HTTP

| Código | Descripción |
|--------|-------------|
| 200 OK | Solicitud exitosa |
| 201 Created | Recurso creado exitosamente |
| 204 No Content | Eliminación exitosa |
| 400 Bad Request | Datos de entrada inválidos |
| 404 Not Found | Recurso no encontrado |
| 500 Internal Server Error | Error del servidor |

## 🔒 Validaciones

### Libro:
- `titulo`: Requerido, longitud entre 1 y 255 caracteres
- `idAutor`: Requerido, debe existir en la base de datos
- `categoria`: Requerido, longitud entre 1 y 100 caracteres
- `cantidadDisponible`: Requerido, valor >= 0

### Autor:
- `nombre`: Requerido, longitud entre 1 y 255 caracteres

### Usuario:
- `nombre`: Requerido, longitud entre 1 y 255 caracteres
- `correoElectronico`: Requerido, formato de email válido, único

### Préstamo:
- `idUsuario`: Requerido, debe existir en la base de datos
- `idLibro`: Requerido, debe existir y tener cantidad disponible > 0
- `fechaPrestamo`: Opcional, por defecto fecha actual

## 📝 Formato de Errores

Todos los errores siguen este formato estándar:

```json
{
  "timestamp": "2026-01-29T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Descripción del error",
  "path": "/api/libros"
}
```
