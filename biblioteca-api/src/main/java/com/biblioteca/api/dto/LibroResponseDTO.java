package com.biblioteca.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroResponseDTO {

    private Long idLibro;
    private String titulo;
    private AutorDTO autor;
    private String categoria;
    private Integer cantidadDisponible;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AutorDTO {
        private Long idAutor;
        private String nombre;
    }
}
