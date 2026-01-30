package com.biblioteca.api.controller;

import com.biblioteca.api.dto.LibroRequestDTO;
import com.biblioteca.api.dto.LibroResponseDTO;
import com.biblioteca.api.service.LibroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroService libroService;

    /**
     * GET /api/libros
     * Obtiene todos los libros
     */
    @GetMapping
    public ResponseEntity<List<LibroResponseDTO>> getAllLibros() {
        List<LibroResponseDTO> libros = libroService.getAllLibros();
        return ResponseEntity.ok(libros);
    }

    /**
     * GET /api/libros/{id}
     * Obtiene un libro por su ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> getLibroById(@PathVariable Long id) {
        LibroResponseDTO libro = libroService.getLibroById(id);
        return ResponseEntity.ok(libro);
    }

    /**
     * POST /api/libros
     * Crea un nuevo libro
     */
    @PostMapping
    public ResponseEntity<LibroResponseDTO> createLibro(
            @Valid @RequestBody LibroRequestDTO libroRequest) {
        LibroResponseDTO libroCreado = libroService.createLibro(libroRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(libroCreado);
    }

    /**
     * PUT /api/libros/{id}
     * Actualiza un libro existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> updateLibro(
            @PathVariable Long id,
            @Valid @RequestBody LibroRequestDTO libroRequest) {
        LibroResponseDTO libroActualizado = libroService.updateLibro(id, libroRequest);
        return ResponseEntity.ok(libroActualizado);
    }

    /**
     * DELETE /api/libros/{id}
     * Elimina un libro
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibro(@PathVariable Long id) {
        libroService.deleteLibro(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/libros/autor/{nombreAutor}
     * Busca libros por nombre de autor
     */
    @GetMapping("/autor/{nombreAutor}")
    public ResponseEntity<List<LibroResponseDTO>> getLibrosByAutor(
            @PathVariable String nombreAutor) {
        List<LibroResponseDTO> libros = libroService.getLibrosByAutorNombre(nombreAutor);
        return ResponseEntity.ok(libros);
    }

    /**
     * GET /api/libros/categoria/{categoria}
     * Busca libros por categoría
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<LibroResponseDTO>> getLibrosByCategoria(
            @PathVariable String categoria) {
        List<LibroResponseDTO> libros = libroService.getLibrosByCategoria(categoria);
        return ResponseEntity.ok(libros);
    }
}
