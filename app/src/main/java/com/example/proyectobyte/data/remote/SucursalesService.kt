package com.example.proyectobyte.data.remote

import com.example.proyectobyte.models.Sucursal
import retrofit2.http.GET

interface SucursalesService {
    @GET("sucursales.php")
    suspend fun getSucursales(): List<Sucursal>
}
