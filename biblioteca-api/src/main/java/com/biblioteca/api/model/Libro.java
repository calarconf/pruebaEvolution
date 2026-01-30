package com.biblioteca.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "libros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_libro")
    private Long idLibro;

    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_autor", nullable = false)
    @JsonIgnoreProperties("libros")
    private Autor autor;

    @Column(name = "categoria", nullable = false, length = 100)
    private String categoria;

    @Column(name = "cantidad_disponible", nullable = false)
    private Integer cantidadDisponible;
}
