package com.biblioteca.api.repository;

import com.biblioteca.api.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    
    Optional<Autor> findByNombre(String nombre);
}
