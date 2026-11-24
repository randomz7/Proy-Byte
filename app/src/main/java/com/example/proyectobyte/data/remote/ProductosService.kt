package com.example.proyectobyte.data.remote

import com.example.proyectobyte.models.Producto
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductosService {
    @GET("productos.php")
    suspend fun getProductos(): List<Producto>

    @GET("producto.php")
    suspend fun getProducto(@Query("id") id: Int): Producto
}
