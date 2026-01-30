package com.biblioteca.app.model

data class Libro(
    val idLibro: Long,
    val titulo: String,
    val autor: Autor,
    val categoria: String,
    val cantidadDisponible: Int
)
