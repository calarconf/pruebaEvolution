# Biblioteca API - Guía de Ejecución Rápida

## 📋 Pasos para Ejecutar

### 1. Navegar al Proyecto
```powershell
cd c:\Users\ca22a\Documents\PruebaEvolution\biblioteca-api
```

### 2. Actualizar Configuración de Base de Datos

Edita el archivo `src/main/resources/application.properties` y actualiza:
- `spring.datasource.username` con tu usuario de PostgreSQL
- `spring.datasource.password` con tu contraseña de PostgreSQL

### 3. Compilar y Ejecutar

```powershell
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

### 4. Probar los Endpoints

#### Listar todos los libros
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/libros" -Method Get
```

#### Crear un nuevo libro
```powershell
$body = @{
    titulo = "Nuevo Libro"
    idAutor = 1
    categoria = "Ficción"
    cantidadDisponible = 5
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/libros" -Method Post -Body $body -ContentType "application/json"
```

## 📝 Notas Importantes

- La aplicación corre en: `http://localhost:8080`
- Endpoints base: `/api/libros`
- Asegúrate de que PostgreSQL esté ejecutándose
- La base de datos debe tener las tablas creadas (ejecuta `crear_base_datos.sql` si no lo has hecho)
