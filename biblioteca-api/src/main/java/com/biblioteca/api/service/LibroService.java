package com.biblioteca.api.service;

import com.biblioteca.api.dto.LibroRequestDTO;
import com.biblioteca.api.dto.LibroResponseDTO;
import com.biblioteca.api.exception.BadRequestException;
import com.biblioteca.api.exception.ResourceNotFoundException;
import com.biblioteca.api.model.Autor;
import com.biblioteca.api.model.Libro;
import com.biblioteca.api.repository.AutorRepository;
import com.biblioteca.api.repository.LibroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibroService {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    /**
     * Obtiene todos los libros
     */
    @Transactional(readOnly = true)
    public List<LibroResponseDTO> getAllLibros() {
        return libroRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un libro por su ID
     */
    @Transactional(readOnly = true)
    public LibroResponseDTO getLibroById(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Libro no encontrado con ID: " + id));
        return convertToDTO(libro);
    }

    /**
     * Crea un nuevo libro
     */
    @Transactional
    public LibroResponseDTO createLibro(LibroRequestDTO libroRequest) {
        // Validar que el autor existe
        Autor autor = autorRepository.findById(libroRequest.getIdAutor())
                .orElseThrow(() -> new BadRequestException(
                        "Autor no encontrado con ID: " + libroRequest.getIdAutor()));

        // Crear nuevo libro
        Libro libro = new Libro();
        libro.setTitulo(libroRequest.getTitulo());
        libro.setAutor(autor);
        libro.setCategoria(libroRequest.getCategoria());
        libro.setCantidadDisponible(libroRequest.getCantidadDisponible());

        // Guardar y retornar
        Libro libroGuardado = libroRepository.save(libro);
        return convertToDTO(libroGuardado);
    }

    /**
     * Actualiza un libro existente
     */
    @Transactional
    public LibroResponseDTO updateLibro(Long id, LibroRequestDTO libroRequest) {
        // Verificar que el libro existe
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Libro no encontrado con ID: " + id));

        // Validar que el autor existe
        Autor autor = autorRepository.findById(libroRequest.getIdAutor())
                .orElseThrow(() -> new BadRequestException(
                        "Autor no encontrado con ID: " + libroRequest.getIdAutor()));

        // Actualizar campos
        libro.setTitulo(libroRequest.getTitulo());
        libro.setAutor(autor);
        libro.setCategoria(libroRequest.getCategoria());
        libro.setCantidadDisponible(libroRequest.getCantidadDisponible());

        // Guardar y retornar
        Libro libroActualizado = libroRepository.save(libro);
        return convertToDTO(libroActualizado);
    }

    /**
     * Elimina un libro
     */
    @Transactional
    public void deleteLibro(Long id) {
        if (!libroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Libro no encontrado con ID: " + id);
        }
        libroRepository.deleteById(id);
    }

    /**
     * Busca libros por nombre de autor
     */
    @Transactional(readOnly = true)
    public List<LibroResponseDTO> getLibrosByAutorNombre(String nombreAutor) {
        return libroRepository.findByAutorNombre(nombreAutor).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca libros por categoría
     */
    @Transactional(readOnly = true)
    public List<LibroResponseDTO> getLibrosByCategoria(String categoria) {
        return libroRepository.findByCategoria(categoria).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad Libro a LibroResponseDTO
     */
    private LibroResponseDTO convertToDTO(Libro libro) {
        LibroResponseDTO.AutorDTO autorDTO = new LibroResponseDTO.AutorDTO(
                libro.getAutor().getIdAutor(),
                libro.getAutor().getNombre()
        );

        return new LibroResponseDTO(
                libro.getIdLibro(),
                libro.getTitulo(),
                autorDTO,
                libro.getCategoria(),
                libro.getCantidadDisponible()
        );
    }
}
