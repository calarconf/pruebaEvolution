package com.biblioteca.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutorRequestDTO {

    @NotBlank(message = "El nombre del autor es obligatorio")
    @Size(min = 1, max = 255, message = "El nombre debe tener entre 1 y 255 caracteres")
    private String nombre;
}
