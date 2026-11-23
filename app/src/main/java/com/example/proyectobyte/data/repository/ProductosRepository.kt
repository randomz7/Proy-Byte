package com.example.proyectobyte.data.repository

import com.example.proyectobyte.data.remote.ProductosService
import com.example.proyectobyte.data.remote.RetrofitClient
import com.example.proyectobyte.models.Producto

class ProductosRepository(
    private val service: ProductosService = RetrofitClient.productosService
) {
    suspend fun listProductos(): List<Producto> = service.getProductos()
}
