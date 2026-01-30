package com.biblioteca.app.api

import com.biblioteca.app.model.Libro
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    
    @GET("api/libros")
    suspend fun getLibros(): Response<List<Libro>>
}
