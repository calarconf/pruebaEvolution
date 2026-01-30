package com.biblioteca.api.repository;

import com.biblioteca.api.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    
    List<Prestamo> findByUsuarioIdUsuario(Long idUsuario);
    
    List<Prestamo> findByLibroIdLibro(Long idLibro);
    
    List<Prestamo> findByFechaDevolucionIsNull();
}
