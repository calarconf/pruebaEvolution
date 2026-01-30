package com.biblioteca.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroRequestDTO {

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 1, max = 255, message = "El título debe tener entre 1 y 255 caracteres")
    private String titulo;

    @NotNull(message = "El ID del autor es obligatorio")
    private Long idAutor;

    @NotBlank(message = "La categoría es obligatoria")
    @Size(min = 1, max = 100, message = "La categoría debe tener entre 1 y 100 caracteres")
    private String categoria;

    @NotNull(message = "La cantidad disponible es obligatoria")
    @Min(value = 0, message = "La cantidad disponible debe ser mayor o igual a 0")
    private Integer cantidadDisponible;
}
