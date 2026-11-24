package com.example.proyectobyte.data.repository

import com.example.proyectobyte.data.remote.EstadisticasService
import com.example.proyectobyte.data.remote.RetrofitClient
import com.example.proyectobyte.models.Dato

class EstadisticasRepository(
    private val service: EstadisticasService = RetrofitClient.estadisticasService
) {
    suspend fun listStockPorMarca(): List<Dato> = service.getStockPorMarca()
}
