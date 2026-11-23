package com.example.proyectobyte.models

data class Sucursal(
    val id: Int = 0,
    val nombre: String = "",
    val direccion: String = "",
    val distrito: String = "",
    val telefono: String = "",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0
)
