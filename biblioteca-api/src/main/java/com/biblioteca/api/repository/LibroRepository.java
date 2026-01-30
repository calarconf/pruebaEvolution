package com.biblioteca.api.repository;

import com.biblioteca.api.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    @Query("SELECT l FROM Libro l JOIN FETCH l.autor a WHERE a.nombre = :nombreAutor")
    List<Libro> findByAutorNombre(@Param("nombreAutor") String nombreAutor);

    List<Libro> findByCategoria(String categoria);
}
