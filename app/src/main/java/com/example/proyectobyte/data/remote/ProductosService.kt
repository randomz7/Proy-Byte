package com.example.proyectobyte.data.remote

import com.example.proyectobyte.models.Producto
import retrofit2.http.GET

interface ProductosService {
    @GET("productos.php")
    suspend fun getProductos(): List<Producto>
}
