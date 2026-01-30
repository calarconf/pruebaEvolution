# 📱 Aplicación Android - Biblioteca App

## ✅ Implementación Completada

La aplicación Android para consumir la API REST de la biblioteca ha sido creada exitosamente.

## 📁 Archivos Creados

### Modelos de Datos
- `app/src/main/java/com/biblioteca/app/model/Autor.kt`
- `app/src/main/java/com/biblioteca/app/model/Libro.kt`

### API Client
- `app/src/main/java/com/biblioteca/app/api/ApiService.kt` - Interface Retrofit
- `app/src/main/java/com/biblioteca/app/api/RetrofitClient.kt` - Cliente HTTP

### UI
- `app/src/main/java/com/biblioteca/app/MainActivity.kt` - Pantalla principal con Jetpack Compose

### Configuración
- `app/build.gradle` - Dependencias actualizadas
- `app/src/main/AndroidManifest.xml` - Permisos de Internet

---

## 🚀 Cómo Ejecutar la Aplicación

### 1. Sincronizar Gradle
En Android Studio:
- Verás un banner arriba que dice "Gradle files have changed"
- Click en **"Sync Now"**
- Espera a que termine la sincronización (puede tardar 1-2 minutos)

### 2. Asegurar que la API está Ejecutándose
```powershell
cd C:\Users\ca22a\Documents\PruebaEvolution\biblioteca-api
mvn spring-boot:run
```
La API debe estar corriendo en `http://localhost:8080`

### 3. Ejecutar la App en el Emulador
1. En Android Studio, click en el botón **▶ Run 'app'** (o presiona Shift+F10)
2. Selecciona un dispositivo virtual (emulador) o créalo si no existe:
   - **Tools → Device Manager → Create Device**
   - Selecciona un dispositivo (ej: Pixel 6)
   - Descarga la imagen del sistema (Android 13 o superior)

### 4. Probar la Aplicación
- Al iniciar, la app automáticamente cargará la lista de libros
- Si hay error, presiona el botón **"Recargar Libros"**

---

## 🔧 Configuración de Red

### Para Emulador Android
La app está configurada para usar `http://10.0.2.2:8080` que es la IP especial que el emulador usa para acceder a `localhost` de tu PC.

**No necesitas cambiar nada si usas el emulador.**

### Para Dispositivo Físico
Si quieres probar en un dispositivo físico:

1. Conecta tu PC y dispositivo a la misma red WiFi
2. Obtén la IP de tu PC:
   ```powershell
   ipconfig
   ```
   Busca "IPv4 Address" (ej: 192.168.1.10)

3. Edita `RetrofitClient.kt`:
   ```kotlin
   private const val BASE_URL = "http://TU_IP_AQUI:8080/"
   ```

---

## 📊 Funcionalidades Implementadas

✅ **Consumo de API REST**
- Conexión a `GET /api/libros`
- Mapeo automático de JSON a objetos Kotlin con Gson

✅ **Interfaz de Usuario**
- Lista de libros con scroll
- Card para cada libro mostrando:
  - Título
  - Autor
  - Categoría
  - Cantidad disponible (con color según disponibilidad)

✅ **Manejo de Errores**
- Mensaje de error detallado si falla la conexión
- Indicador de carga mientras obtiene datos
- Botón de recarga manual
- Validación de respuestas HTTP

✅ **Experiencia de Usuario**
- Interfaz moderna con Material Design 3
- Loading spinner durante carga
- Mensajes informativos
- Diseño responsive

---

## 🎨 Capturas de Funcionalidad

### Estado de Carga
```
┌─────────────────────────────────┐
│  Biblioteca - Lista de Libros   │
├─────────────────────────────────┤
│  [    Cargando...    ]          │
│                                 │
│         ⏳                       │
│                                 │
└─────────────────────────────────┘
```

### Lista de Libros
```
┌─────────────────────────────────┐
│  Biblioteca - Lista de Libros   │
├─────────────────────────────────┤
│  [  Recargar Libros  ]          │
│                                 │
│  ┌───────────────────────────┐  │
│  │ Cien años de soledad      │  │
│  │ Autor: Gabriel G. Márquez │  │
│  │ Categoría: Ficción        │  │
│  │ Disponibles: 5            │  │
│  └───────────────────────────┘  │
│                                 │
│  ┌───────────────────────────┐  │
│  │ La casa de los espíritus  │  │
│  │ Autor: Isabel Allende     │  │
│  │ Categoría: Ficción        │  │
│  │ Disponibles: 4            │  │
│  └───────────────────────────┘  │
└─────────────────────────────────┘
```

### Error de Conexión
```
┌─────────────────────────────────┐
│  Biblioteca - Lista de Libros   │
├─────────────────────────────────┤
│  [  Recargar Libros  ]          │
│                                 │
│  ┌───────────────────────────┐  │
│  │ ⚠️ Error de conexión       │  │
│  │                           │  │
│  │ Asegúrate de que:         │  │
│  │ 1. La API esté en :8080   │  │
│  │ 2. Usa http://10.0.2.2    │  │
│  └───────────────────────────┘  │
└─────────────────────────────────┘
```

---

## 🐛 Solución de Problemas

### Error: "Unable to resolve dependency"
**Solución**: Click en "Sync Now" en Android Studio

### Error: "Failed to connect to /10.0.2.2:8080"
**Causa**: La API Spring Boot no está ejecutándose
**Solución**: 
```powershell
cd biblioteca-api
mvn spring-boot:run
```

### Error: "Cleartext HTTP traffic not permitted"
**Solución**: Ya está resuelto con `android:usesCleartextTraffic="true"` en AndroidManifest.xml

### La app muestra "No hay libros"
**Causa**: La base de datos está vacía
**Solución**: Ejecuta el script SQL para insertar datos de prueba

---

## 📝 Código Clave Explicado

### Consumo de API (RetrofitClient.kt)
```kotlin
// Retrofit convierte automáticamente el JSON de la API
// a objetos Libro usando Gson
val response = RetrofitClient.apiService.getLibros()
if (response.isSuccessful) {
    libros = response.body() ?: emptyList()
}
```

### Manejo Asíncrono (MainActivity.kt)
```kotlin
// Coroutines para operaciones en segundo plano
scope.launch {
    isLoading = true
    try {
        // Llamada a la API sin bloquear la UI
        val response = RetrofitClient.apiService.getLibros()
        // Actualiza la UI en el hilo principal
        libros = response.body() ?: emptyList()
    } catch (e: Exception) {
        errorMessage = e.message
    } finally {
        isLoading = false
    }
}
```

### UI Reactiva (Jetpack Compose)
```kotlin
// La UI se actualiza automáticamente cuando cambia el estado
var libros by remember { mutableStateOf<List<Libro>>(emptyList()) }

// LazyColumn = RecyclerView optimizado
LazyColumn {
    items(libros) { libro ->
        LibroCard(libro)
    }
}
```

---

## ✨ Tecnologías Utilizadas

| Tecnología | Propósito |
|------------|-----------|
| **Kotlin** | Lenguaje de programación |
| **Jetpack Compose** | Framework de UI moderno |
| **Retrofit** | Cliente HTTP para consumir API REST |
| **Gson** | Serialización/deserialización JSON |
| **Coroutines** | Programación asíncrona |
| **Material 3** | Diseño de interfaz |

---

## 🎯 Cumplimiento de Requisitos

✅ **Consumo de API REST**: La app consume el endpoint `GET /api/libros`  
✅ **Lenguaje Java/Kotlin**: Implementado en Kotlin (lenguaje oficial de Android)  
✅ **Manejo de Respuestas**: Parsing automático de JSON con Gson  
✅ **Manejo de Errores**: Try-catch con mensajes informativos  
✅ **UI Funcional**: Lista completa de libros con diseño profesional  

---

## 📚 Próximos Pasos (Opcional)

Si quieres extender la aplicación, podrías agregar:

1. **Búsqueda de libros** por título o autor
2. **Detalle del libro** al hacer click en una tarjeta
3. **Agregar nuevo libro** desde la app
4. **Pull to refresh** para actualizar la lista
5. **Caché local** con Room Database
6. **Modo offline** guardando datos localmente

---

## 📞 Resumen de Ejecución

```powershell
# 1. Iniciar API Backend
cd C:\Users\ca22a\Documents\PruebaEvolution\biblioteca-api
mvn spring-boot:run

# 2. En Android Studio
# - Sync Gradle
# - Run 'app' (Shift+F10)
# - Esperar que inicie el emulador
# - La app cargará automáticamente los libros
```

**¡La aplicación está lista para usar! 🎉**
