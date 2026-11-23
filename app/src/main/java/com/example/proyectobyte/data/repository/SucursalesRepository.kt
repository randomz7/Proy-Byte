package com.example.proyectobyte.data.repository

import com.example.proyectobyte.data.remote.RetrofitClient
import com.example.proyectobyte.data.remote.SucursalesService
import com.example.proyectobyte.models.Sucursal

class SucursalesRepository(
    private val service: SucursalesService = RetrofitClient.sucursalesService
) {
    suspend fun listSucursales(): List<Sucursal> = service.getSucursales()
}
