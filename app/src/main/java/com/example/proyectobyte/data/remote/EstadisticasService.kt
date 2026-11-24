package com.example.proyectobyte.data.remote

import com.example.proyectobyte.models.Dato
import retrofit2.http.GET

interface EstadisticasService {
    @GET("stock_por_marca.php")
    suspend fun getStockPorMarca(): List<Dato>
}
