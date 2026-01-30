package com.biblioteca.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.biblioteca.app.api.RetrofitClient
import com.biblioteca.app.model.Libro
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                BibliotecaApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibliotecaApp() {
    var libros by remember { mutableStateOf<List<Libro>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Función para cargar libros
    fun cargarLibros() {
        scope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = RetrofitClient.apiService.getLibros()
                if (response.isSuccessful) {
                    libros = response.body() ?: emptyList()
                    errorMessage = if (libros.isEmpty()) "No hay libros disponibles" else null
                } else {
                    errorMessage = "Error: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                errorMessage = "Error de conexión: ${e.message}\n\nAsegúrate de que:\n" +
                        "1. La API esté ejecutándose en http://localhost:8080\n" +
                        "2. El emulador use http://10.0.2.2:8080"
            } finally {
                isLoading = false
            }
        }
    }

    // Cargar libros al iniciar
    LaunchedEffect(Unit) {
        cargarLibros()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Biblioteca - Lista de Libros") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Botón de recargar
            Button(
                onClick = { cargarLibros() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text(if (isLoading) "Cargando..." else "Recargar Libros")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar estado
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                errorMessage != null -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = errorMessage!!,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }

                libros.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay libros para mostrar")
                    }
                }

                else -> {
                    // Lista de libros
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(libros) { libro ->
                            LibroCard(libro)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LibroCard(libro: Libro) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = libro.titulo,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Autor: ${libro.autor.nombre}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Categoría: ${libro.categoria}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Disponibles: ${libro.cantidadDisponible}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (libro.cantidadDisponible > 0) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.error
            )
        }
    }
}